package ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GasData {

    @ApiModelProperty(example = "1000000")
    String gasLimit;

    @ApiModelProperty(example = "0")
    String gasPrice;

}
