package ru.mockingrave.ethereum.javabackend.geth.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mockingrave.ethereum.javabackend.geth.dto.CertificateDto;
import ru.mockingrave.ethereum.javabackend.ipfs.dto.IpfsCertificateDto;
import ru.mockingrave.ethereum.javabackend.geth.service.CertifierService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/certifier")
public class CertifierController {

    @Autowired
    private final CertifierService cService;

    @GetMapping("/{ipfsHash}")
    public ResponseEntity<CertificateDto> getCertificate(@PathVariable String ipfsHash) {
        return ResponseEntity.ok()
                .body(cService.getCertificate(ipfsHash));
    }

    @GetMapping("/check/{ipfsHash}")
    public ResponseEntity<String> checkCertificate
            (@PathVariable String ipfsHash) {
        String response = "The certificate does not exist.";
        if (cService.checkCertificate(ipfsHash))
            response = "The certificate is valid.";
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate
            (@RequestBody IpfsCertificateDto dto, String ipfsHash, String password) {
        return ResponseEntity.ok()
                .body(cService.createCertificate(dto, ipfsHash, password));
    }

    @PutMapping("/{oldIpfsHash}")
    public ResponseEntity<CertificateDto> updateCertificate
            (@PathVariable String oldIpfsHash, @RequestBody IpfsCertificateDto dto, String walletName, String password) {
        return ResponseEntity.ok()
                .body(cService.updateCertificateSource(oldIpfsHash, dto, walletName, password));
    }

    @DeleteMapping("/{deleteIpfsHash}")
    public ResponseEntity<String> deleteCertificate
            (@PathVariable String deleteIpfsHash, @RequestBody String sourceIpfsHash, String walletName, String password) {
        String response = "Failed";
        if (cService.deleteCertificate(deleteIpfsHash, sourceIpfsHash, walletName, password))
            response = "Success";
        return ResponseEntity.ok()
                .body(response);
    }
}
