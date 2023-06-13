package ru.mockingrave.ethereum.javabackend.geth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthAuthorityDto implements Serializable {

    @ApiModelProperty(name = "(Источник полномочий) Hash для доступа к данным в IPFS")
    String sourceAccreditorIpfsHash;

    @ApiModelProperty(name = "Адрес в сети Ethereum")
    String ethAddress;

    @ApiModelProperty(name = " Hash для доступа к данным в IPFS")
    String ipfsHash;

    @ApiModelProperty
    String password;
}
