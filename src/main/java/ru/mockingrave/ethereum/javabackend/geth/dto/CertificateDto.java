package ru.mockingrave.ethereum.javabackend.geth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.Certificate;
import ru.mockingrave.ethereum.javabackend.ipfs.dto.IpfsCertificateDto;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDto {

    IpfsCertificateDto mainInfo;

    String ipfsHash;

    public CertificateDto(Certificate model) {
        this.mainInfo = new IpfsCertificateDto(
                null,
                null,
                model.getReceivingDate(),
                model.getOwnerName(),
                null,
                model.getQualifications(),
                model.getContacts(),
                model.getNote());
        this.ipfsHash = model.getIpfsHash();
    }
}
