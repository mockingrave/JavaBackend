package ru.mockingrave.ethereum.javabackend.geth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.AuthenticationData;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.GasData;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    @ApiModelProperty(position = 3)
    AuthenticationData authenticationData;

    @ApiModelProperty(name = "Адрес получателя", position = 0)
    String addressTo;

    @ApiModelProperty(name = "Переводимое значение (eth)",position = 1)
    String value;

    @ApiModelProperty(position = 2)
    GasData gasData;

}
