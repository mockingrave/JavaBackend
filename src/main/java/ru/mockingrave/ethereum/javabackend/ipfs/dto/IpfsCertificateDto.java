package ru.mockingrave.ethereum.javabackend.ipfs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.Qualification;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpfsCertificateDto implements Serializable {

    //auto
    String accreditorName;
    String certifierName;
    String receivingDate;
    String certifierIpfsHash;

    //input
    String ownerName;

    List<Qualification> qualifications;

    String contacts;

    String note;
}
