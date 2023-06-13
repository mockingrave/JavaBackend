package ru.mockingrave.ethereum.javabackend.elasticsearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.Certificate;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.SkillLevel;
import ru.mockingrave.ethereum.javabackend.elasticsearch.repository.CertificateRepository;

import java.util.List;

@Service
public class ElasticsearchService {

    @Autowired
    private CertificateRepository certificateRepository;

    public void addCertificate(Certificate certificate) {
        certificate.getQualifications().forEach(qualification -> {
            qualification.setSkill(qualification.getSkill().toUpperCase().trim());
        });
        certificateRepository.save(certificate);
    }

    public void updateCertificate(Certificate certificate) {

        try {
            certificateRepository.deleteById(certificate.getIpfsHash());
        } catch (Exception ignored) {
        }
        ;

        certificate.getQualifications().forEach(qualification -> {
            qualification.setSkill(qualification.getSkill().toUpperCase().trim());
        });
        certificateRepository.save(certificate);
    }

    public void deleteCertificate(String ipfsHash) {
        certificateRepository.deleteById(ipfsHash);
    }


    public List<Certificate> getByQualifications(List<String> skills, List<SkillLevel> levels) {
        if (skills!=null&&skills.isEmpty()) skills = null;
        if (levels!=null&&levels.isEmpty()) levels = null;

        if (skills != null && levels != null) return certificateRepository.findAllByQualifications_SkillInAndQualifications_LevelIn(skills, levels);
        if (skills != null && levels == null) return certificateRepository.findAllByQualifications_SkillIn(skills);
        if (skills == null && levels != null) return certificateRepository.findAllByQualifications_LevelIn(levels);

        return certificateRepository.findAll();
    }

}

