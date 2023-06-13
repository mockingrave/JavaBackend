package ru.mockingrave.ethereum.javabackend.geth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mockingrave.ethereum.javabackend.geth.dto.DeployDto;
import ru.mockingrave.ethereum.javabackend.geth.dto.EthAccountDto;
import ru.mockingrave.ethereum.javabackend.geth.dto.InfoDto;
import ru.mockingrave.ethereum.javabackend.geth.dto.TransactionDto;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.AuthenticationData;
import ru.mockingrave.ethereum.javabackend.geth.service.GethContractService;
import ru.mockingrave.ethereum.javabackend.geth.service.GethService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/geth")
public class GethController {

    private final GethService gethService;
    private final GethContractService deployService;


    @GetMapping("/connectionTest")
    public ResponseEntity<InfoDto> checkConnection() {
        return ResponseEntity.ok()
                .body(null);
    }

    @PostMapping("/account")
    public ResponseEntity<EthAccountDto> createAccount(@RequestBody String password) {
        return ResponseEntity.ok()
                .body(gethService.createNewAccount(password));
    }

    @PostMapping("/account/check")
    public ResponseEntity<EthAccountDto> checkAccount(@RequestBody AuthenticationData account) {
        return ResponseEntity.ok()
                .body(gethService.checkAccount(account.getWalletName(), account.getPassword()));
    }

    @PostMapping("/account/transfer")
    public ResponseEntity<InfoDto> transferMoney(@RequestBody TransactionDto dto) {
        return ResponseEntity.ok()
                .body(gethService.transferMoney(
                        dto.getAuthenticationData().getWalletName(),
                        dto.getAuthenticationData().getPassword(),
                        dto.getAddressTo(),
                        dto.getValue(),
                        dto.getGasData().getGasLimit(),
                        dto.getGasData().getGasPrice()));
    }

    @PostMapping("/contract/deploy")
    public ResponseEntity<String> deploySystemOfContracts
            (@RequestBody DeployDto dto) {
        String response = "Failed. Something is wrong.";
        if (deployService.createOwnerAndDeploySystem(dto))
            response = "Successfully. Contract addresses added to application.yml";
        return ResponseEntity.ok()
                .body(response);
    }

}
