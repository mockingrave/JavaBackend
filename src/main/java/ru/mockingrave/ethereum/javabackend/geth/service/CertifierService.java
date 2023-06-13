package ru.mockingrave.ethereum.javabackend.geth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.DefaultBlockParameterName;
import ru.mockingrave.ethereum.javabackend.elasticsearch.service.ElasticsearchService;
import ru.mockingrave.ethereum.javabackend.geth.dto.CertificateDto;
import ru.mockingrave.ethereum.javabackend.ipfs.dto.IpfsCertificateDto;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.Certificate;
import ru.mockingrave.ethereum.javabackend.ipfs.service.IpfsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CertifierService extends GethService {

    @Value("${geth.account.viewing.wallet}")
    protected String ACC_WALL;
    @Value("${geth.account.viewing.password}")
    protected String ACC_PASS;

    private final GethContractService gethContractService;
    private final IpfsService ipfsService;
    private final AccreditorCertifierService acService;
    private final AccreditorService aService;
    private final ElasticsearchService eService;

    /**
     * IpfsCertificateDto:
     * ownerName
     * certifierIpfsHash
     * qualifications
     * contacts
     * note
     */
    public CertificateDto createCertificate(IpfsCertificateDto newDto, String ipfsHash, String password) {

        //eventsSubscribe();

        var sourceCertifierDto = acService.getIpfsCertifier(ipfsHash);
        var walletName = sourceCertifierDto.getWalletName();

        //load contract
        var certifierContract = gethContractService.certifierContractLoad(walletName, password);

        var accreditorDto = aService.getIpfsAccreditor(sourceCertifierDto.getSourceAccreditorIpfsHash());

        newDto.setAccreditorName(accreditorDto.getCompanyName());
        newDto.setCertifierName(sourceCertifierDto.getCompanyName());
        newDto.setReceivingDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        newDto.setCertifierIpfsHash(ipfsHash);

        //save in IPFS
        var newIpfsHash = ipfsService.serializableToIpfs(newDto);

        //save in Ethereum
        try {
            var credentials = WalletUtils.loadCredentials(password, KEY_PATH + walletName);
            certifierContract.createCertificate(ipfsHash, newIpfsHash, credentials);
        } catch (Exception e) {
            e.printStackTrace(); //TODO: exception handler
        }

        return getCertificate(newIpfsHash);
    }

    public CertificateDto updateCertificateSource
            (String oldHash, IpfsCertificateDto newData, String walletName, String password) {

        eventsSubscribe();

        var certifierContract = gethContractService.certifierContractLoad(walletName, password);

        var oldData = getCertificate(oldHash).getMainInfo();

        newData.setQualifications(oldData.getQualifications());
        if (newData.getAccreditorName().isEmpty())
            newData.setAccreditorName(oldData.getAccreditorName());
        if (newData.getCertifierName().isEmpty())
            newData.setCertifierName(oldData.getCertifierName());
        if (newData.getCertifierIpfsHash().isEmpty())
            newData.setCertifierIpfsHash(oldData.getCertifierIpfsHash());
        newData.setOwnerName(oldData.getOwnerName());
        newData.setReceivingDate(oldData.getReceivingDate());
        if (newData.getNote()!=oldData.getNote())
            newData.setNote(oldData.getNote());

        var newIpfsHash = ipfsService.serializableToIpfs(newData);

        try {
            var credentials = WalletUtils.loadCredentials(password, KEY_PATH + walletName);
            certifierContract.updateCertificateSource(newData.getCertifierIpfsHash(), newIpfsHash, credentials);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getCertificate(newIpfsHash);
    }

    public boolean deleteCertificate(String deleteIpfsHash, String sourceIpfsHash, String walletName, String password) {
        eventsSubscribe();

        var certifierContract = gethContractService.certifierContractLoad(walletName, password);

        try {
            var credentials = WalletUtils.loadCredentials(password, KEY_PATH + walletName);
            certifierContract.deleteCertificate(sourceIpfsHash, deleteIpfsHash, credentials);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return !checkCertificate(deleteIpfsHash);
    }

    public CertificateDto getCertificate(String ipfsHash) {

        var response = new CertificateDto();
        response.setIpfsHash(ipfsHash);
        response.setMainInfo((IpfsCertificateDto) ipfsService.serializableFromIpfs(ipfsHash));
        return response;

    }

    public String getEthCertificate(String ipfsHash) {

        var certifierContract = gethContractService.certifierContractLoad(ACC_WALL, ACC_PASS);
        try {
            return certifierContract.getCertificate(ipfsHash).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkCertificate(String ipfsHash) {

        var certifierContract = gethContractService.certifierContractLoad(ACC_WALL, ACC_PASS);
        try {
            return certifierContract.checkCertificate(ipfsHash).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void eventsSubscribe(){

        var certifierContract = gethContractService.certifierContractLoad(ACC_WALL, ACC_PASS);

        certifierContract
                .logCreateCertificateEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    String ipfsHash = event.ipfsHash;
                    String certifier = event.sender;

                    eService.addCertificate(new Certificate(getCertificate(ipfsHash)));
                }, throwable -> System.out.println("ThrowableThrowableThrowableThrowable " + throwable.getMessage()));

        certifierContract
                .logUpdateCertificateEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    String ipfsHash = event.ipfsHash;
                    String certifier = event.sender;

                    eService.updateCertificate(new Certificate(getCertificate(ipfsHash)));
                }, throwable -> System.out.println("ThrowableThrowableThrowableThrowable " + throwable.getMessage()));

        certifierContract
                .logDeleteCertificateEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    String deleteIpfsHash = event.deleteIpfsHash;
                    String certifier = event.sender;

                    eService.deleteCertificate(deleteIpfsHash);
                }, throwable -> System.out.println("ThrowableThrowableThrowableThrowable " + throwable.getMessage()));
    }

}
