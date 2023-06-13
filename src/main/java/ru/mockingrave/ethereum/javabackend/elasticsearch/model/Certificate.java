package ru.mockingrave.ethereum.javabackend.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import ru.mockingrave.ethereum.javabackend.geth.dto.CertificateDto;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.Qualification;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(indexName = "certificate")
public class Certificate {

    @Id
    String ipfsHash;

    @Field(type = FieldType.Auto, includeInParent = true)
    List<Qualification> qualifications;

    @Field(type = FieldType.Text)
    String ownerName;

    @Field(type = FieldType.Text)
    String contacts;

    @Field(type = FieldType.Text)
    String receivingDate;

    @Field(type = FieldType.Text)
    String note;

    public Certificate(CertificateDto dto) {
        this.ipfsHash = dto.getIpfsHash();
        this.qualifications = dto.getMainInfo().getQualifications();
        this.ownerName = dto.getMainInfo().getOwnerName();
        this.contacts = dto.getMainInfo().getContacts();
        this.receivingDate = dto.getMainInfo().getReceivingDate();
        this.note = dto.getMainInfo().getNote();
    }
}
