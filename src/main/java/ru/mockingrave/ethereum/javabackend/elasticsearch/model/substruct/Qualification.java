package ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Document(indexName = "qualification")
public class Qualification implements Serializable {

    @Id
    String skill;

    @Field(type = FieldType.Auto)
    SkillLevel level;
}
