package ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationData implements Serializable {

    @ApiModelProperty(allowableValues = "range(82,82)", position = 0)
    String walletName;

    String password;

}
