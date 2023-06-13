package ru.mockingrave.ethereum.javabackend.geth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.AuthenticationData;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.GasData;

@Data
public class DeployDto{

    @ApiModelProperty(name = "Имя компании")
    String companyName;

    @ApiModelProperty(name = "Название подразделения")
    String departmentName;

    @ApiModelProperty(name = "Физический адрес подразделения")
    String address;

    GasData gasData;

    AuthenticationData authenticationData;

    public DeployDto(){
        authenticationData = new AuthenticationData();
        gasData = new GasData();
    }

    public void setWallet(String walletName) {
        this.authenticationData.setWalletName(walletName);
    }

    public void setPassword(String password) {
        this.authenticationData.setPassword(password);
    }

    public void setGasLimit(String gasLimit) {
        this.getGasData().setGasLimit(gasLimit);
    }

    public void setGasPrice(String gasPrice) {
        this.getGasData().setGasPrice(gasPrice);
    }
}
