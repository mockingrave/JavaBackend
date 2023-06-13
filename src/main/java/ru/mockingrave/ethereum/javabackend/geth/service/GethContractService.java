package ru.mockingrave.ethereum.javabackend.geth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import ru.mockingrave.ethereum.javabackend.geth.contracts.impl.AccreditorContract;
import ru.mockingrave.ethereum.javabackend.geth.contracts.impl.CertifierContract;
import ru.mockingrave.ethereum.javabackend.geth.dto.DeployDto;
import ru.mockingrave.ethereum.javabackend.geth.dto.EthAccountDto;
import ru.mockingrave.ethereum.javabackend.ipfs.dto.IpfsAuthorityDto;
import ru.mockingrave.ethereum.javabackend.ipfs.service.IpfsService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
public class GethContractService extends GethService {

    @Value("${geth.contract.accreditor}")
    public String ACCREDITOR_CONTRACT_ADDRESS;
    @Value("${geth.contract.certifier}")
    public String CERTIFIER_CONTRACT_ADDRESS;
    @Autowired
    private IpfsService ipfsService;
    private String PROPERTIES_PATH = "src/main/resources/";

    public boolean createOwnerAndDeploySystem(DeployDto dto) {

        if (!ACCREDITOR_CONTRACT_ADDRESS.isEmpty() || !CERTIFIER_CONTRACT_ADDRESS.isEmpty()) return false;

        String sourceName = KEY_PATH + dto.getAuthenticationData().getWalletName();
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(dto.getAuthenticationData().getPassword(), sourceName);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        if (credentials == null) return false;

        var authorityDto = new IpfsAuthorityDto();

        authorityDto.setCompanyName(dto.getCompanyName());
        authorityDto.setDepartmentName(dto.getDepartmentName());
        authorityDto.setAddress(dto.getAddress());
        authorityDto.setWalletName(credentials.getAddress());

        authorityDto.setSourceAccreditorEthAddress(credentials.getAddress());
        authorityDto.setSourceAccreditorName("(ROOT OWNER) " + dto.getCompanyName());
        authorityDto.setSourceAccreditorIpfsHash("(ROOT OWNER)");

        authorityDto.setActivationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        var ipfsHash = ipfsService.serializableToIpfs(authorityDto);

        certifierContractDeploy(
                ipfsHash,
                dto.getAuthenticationData().getWalletName(),
                dto.getAuthenticationData().getPassword(),
                dto.getGasData().getGasLimit(),
                dto.getGasData().getGasPrice());

        createLocalViewingAccount("password");
        return true;
    }

    public String certifierContractDeploy(String ownerIpfsHash, String ownerWalletName, String password, String gasLimit, String gasPrice) {

        CertifierContract registryContract = null;

        BigInteger bIntGasLimit = BigInteger.valueOf(Long.parseLong(gasLimit));
        BigInteger bIntGasPrice = Convert.toWei(gasPrice, Convert.Unit.ETHER).toBigInteger();

        try {
            Credentials credentials = WalletUtils.loadCredentials(password, KEY_PATH + ownerWalletName);

            var transactionManager = new RawTransactionManager(
                    web3j, credentials, Long.parseLong(web3j.netVersion().send().getNetVersion()));

            registryContract = CertifierContract.deploy
                    (web3j, transactionManager, new StaticGasProvider(bIntGasPrice, bIntGasLimit),
                            accreditorContractDeploy(ownerIpfsHash, ownerWalletName, password, gasLimit, gasPrice)).send();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (registryContract != null) {
            var address = registryContract.getContractAddress();
            saveInProperties("contract.certifier", address);
            return address;
        }
        return null;
    }

    private String accreditorContractDeploy(String ownerIpfsHash, String ownerWalletName, String password, String gasLimit, String gasPrice) {

        AccreditorContract registryContract = null;

        BigInteger bIntGasLimit = BigInteger.valueOf(Long.parseLong(gasLimit));
        BigInteger bIntGasPrice = Convert.toWei(gasPrice, Convert.Unit.ETHER).toBigInteger();

        try {
            Credentials credentials = WalletUtils.loadCredentials(password, KEY_PATH + ownerWalletName);

            var transactionManager = new RawTransactionManager(
                    web3j, credentials, Long.parseLong(web3j.netVersion().send().getNetVersion()));


            registryContract = AccreditorContract.deploy(web3j, transactionManager, new StaticGasProvider(bIntGasPrice, bIntGasLimit), ownerIpfsHash).send();

            if (registryContract != null) {
                var address = registryContract.getContractAddress();
                saveInProperties("contract.accreditor", address);
                saveInProperties("contract.accreditor.owner.ipfs", ownerIpfsHash);
                saveInProperties("contract.accreditor.owner.address", credentials.getAddress());

                if (!registryContract.checkAccreditor(ownerIpfsHash).send()) address = null;
                var a = registryContract.getAccreditorAddress(ownerIpfsHash).send();
                var b = registryContract.getAccreditorSource(ownerIpfsHash).send();
                return address;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private void saveInProperties(String key, String value) {

        var fileName = "application.yml";
        var p = new Properties();

        if(value==null) value="";

        try {
            var is = new FileInputStream(PROPERTIES_PATH + fileName);
            p.load(is);
            p.setProperty(key, value);
            var os = new FileOutputStream(PROPERTIES_PATH + fileName);
            p.store(os, null);
            is.close();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EthAccountDto createLocalViewingAccount(String password){
        var account = createNewAccount(password);
        saveInProperties("account.viewing.address", account.getAddress());
        saveInProperties("account.viewing.wallet", account.getAuthenticationData().getWalletName());
        saveInProperties("account.viewing.password", password);
        return account;
    }

    public AccreditorContract accreditorContractLoad(String walletName, String password) {
        String source = KEY_PATH + walletName;
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password, source);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return AccreditorContract.load(ACCREDITOR_CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
    }

    public CertifierContract certifierContractLoad(String walletName, String password) {
        String source = KEY_PATH + walletName;
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password, source);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return CertifierContract.load(CERTIFIER_CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
    }

}
