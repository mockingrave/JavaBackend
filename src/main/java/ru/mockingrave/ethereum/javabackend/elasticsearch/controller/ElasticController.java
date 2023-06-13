package ru.mockingrave.ethereum.javabackend.elasticsearch.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.Certificate;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.SkillLevel;
import ru.mockingrave.ethereum.javabackend.elasticsearch.service.ElasticsearchService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search")
public class ElasticController {

    @Autowired
    ElasticsearchService eService;

    @GetMapping("/certificate")
    public ResponseEntity<List<Certificate>> getCertificatesByQualifications(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) List<SkillLevel> levels) {
        return ResponseEntity.ok()
                .body(eService.getByQualifications(skills, levels));
    }

    @PostMapping("/certificate")
    public ResponseEntity<Boolean> createCertificate(@RequestBody Certificate dto) {
        eService.addCertificate(dto);
        return ResponseEntity.ok()
                .body(true);
    }

    @DeleteMapping("/certificate")
    public ResponseEntity<Boolean> deleteCertificate(@RequestBody String ipfsHash) {
        eService.deleteCertificate(ipfsHash);
        return ResponseEntity.ok()
                .body(true);
    }
}
