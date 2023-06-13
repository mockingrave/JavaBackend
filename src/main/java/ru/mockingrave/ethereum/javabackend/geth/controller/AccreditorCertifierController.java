package ru.mockingrave.ethereum.javabackend.geth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mockingrave.ethereum.javabackend.geth.dto.EthAuthorityDto;
import ru.mockingrave.ethereum.javabackend.ipfs.dto.IpfsAuthorityDto;
import ru.mockingrave.ethereum.javabackend.geth.service.AccreditorCertifierService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accreditor/certifier")
public class AccreditorCertifierController {

    private final AccreditorCertifierService acService;

    @GetMapping("/{ipfsHash}")
    public ResponseEntity<IpfsAuthorityDto> getCertifier(@PathVariable String ipfsHash) {
        return ResponseEntity.ok()
                .body(acService.getIpfsCertifier(ipfsHash));
    }

    @GetMapping("/eth/{ipfsHash}")
    public ResponseEntity<EthAuthorityDto> getEthCertifier(@PathVariable String ipfsHash) {

        return ResponseEntity.ok()
                .body(acService.getEthCertifier(ipfsHash));
    }

    @GetMapping("/check/{ipfsHash}")
    public ResponseEntity<String> checkCertifier(@PathVariable String ipfsHash) {
        String response = "The Certifier does not exist.";
        if (acService.checkEthCertifier(ipfsHash))
            response = "The Certifier is valid.";
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    public ResponseEntity<EthAuthorityDto> createCertifier(@RequestBody IpfsAuthorityDto dto, String ipfsHash, String password) {
        return ResponseEntity.ok()
                .body(acService.createCertifier(dto, ipfsHash, password));
    }

    @PutMapping("/{oldIpfsHash}")
    public ResponseEntity<EthAuthorityDto> updateCertifier
            (@PathVariable String oldIpfsHash, @RequestBody IpfsAuthorityDto dto, String walletName, String password) {
        return ResponseEntity.ok()
                .body(acService.updateCertifier(oldIpfsHash, dto, walletName, password));
    }

    @DeleteMapping("/{deleteIpfsHash}")
    public ResponseEntity<String> deleteCertifier
            (@PathVariable String deleteIpfsHash, @RequestBody String sourceIpfsHash, String walletName, String password) {
        String response = "Failed";
        if (acService.deleteCertifier(deleteIpfsHash, sourceIpfsHash, walletName, password))
            response = "Success";
        return ResponseEntity.ok()
                .body(response);
    }
}
