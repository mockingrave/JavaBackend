package ru.mockingrave.ethereum.javabackend.geth.controller;

import lombok.RequiredArgsConstructor;
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
import ru.mockingrave.ethereum.javabackend.geth.service.AccreditorService;

@RestController
@RequestMapping("/api/accreditor")
@RequiredArgsConstructor
public class AccreditorController {

    private final AccreditorService accreditorService;

    @GetMapping("/{ipfsHash}")
    public ResponseEntity<IpfsAuthorityDto> getAccreditor(@PathVariable String ipfsHash) {
        return ResponseEntity.ok()
                .body(accreditorService.getIpfsAccreditor(ipfsHash));
    }

    @GetMapping("/eth/{ipfsHash}")
    public ResponseEntity<EthAuthorityDto> getEthAccreditor(@PathVariable String ipfsHash) {

        return ResponseEntity.ok()
                .body(accreditorService.getEthAccreditor(ipfsHash));
    }

    @GetMapping("/check/{ipfsHash}")
    public ResponseEntity<String> checkAccreditor(@PathVariable String ipfsHash) {
        String response = "The Accreditor does not exist.";
        if (accreditorService.checkEthAccreditor(ipfsHash))
            response = "The Accreditor is valid.";
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    public ResponseEntity<EthAuthorityDto> createAccreditor
            (@RequestBody IpfsAuthorityDto dto, String ipfsHash, String password) {

        return ResponseEntity.ok()
                .body(accreditorService.createAccreditor(dto, ipfsHash, password));
    }

    @PutMapping("/{oldIpfsHash}")
    public ResponseEntity<EthAuthorityDto> updateAccreditor
            (@PathVariable String oldIpfsHash, @RequestBody IpfsAuthorityDto dto, String ipfsHash, String password) {
        return ResponseEntity.ok()
                .body(accreditorService.updateAccreditor(oldIpfsHash, dto, ipfsHash, password));
    }

    @DeleteMapping("/{deleteIpfsHash}")
    public ResponseEntity<String> deleteAccreditor
            (@PathVariable String deleteIpfsHash, @RequestBody String sourceIpfsHash, String ipfsHash, String password) {

        if (accreditorService.deleteAccreditor(deleteIpfsHash, sourceIpfsHash, ipfsHash, password)) {
            return ResponseEntity.ok()
                    .body("");
        }
        return null;
    }

}
