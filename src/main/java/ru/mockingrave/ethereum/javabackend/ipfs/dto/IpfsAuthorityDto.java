package ru.mockingrave.ethereum.javabackend.ipfs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpfsAuthorityDto implements Serializable {

    //auto

    @ApiModelProperty(name = "(Служебное) Имя кошелька)")//todo: прикрепить механизм к аккаунту
    String walletName;

    @ApiModelProperty(name = "Дата начала деятельности", readOnly = true, example = "controlled by the system")
    String activationDate;

    @ApiModelProperty(name = "(Источник полномочий) Hash для доступа к данным в IPFS")
    String sourceAccreditorIpfsHash;

    @ApiModelProperty(name = "(Источник полномочий) Имя компании")
    String sourceAccreditorName;

    @ApiModelProperty(name = "(Источник полномочий) Адрес в сети Ethereum")
    String sourceAccreditorEthAddress;


    //input

    @ApiModelProperty(name = "Имя компании")
    String companyName;

    @ApiModelProperty(name = "Имя подразделения, отвечающего за аккаунт")
    String departmentName;

    @ApiModelProperty(name = "Физический адрес")
    String address;

}
