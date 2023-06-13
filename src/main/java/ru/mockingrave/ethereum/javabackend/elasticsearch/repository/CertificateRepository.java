package ru.mockingrave.ethereum.javabackend.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.Certificate;
import ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct.SkillLevel;

import java.util.Collection;
import java.util.List;

public interface CertificateRepository extends ElasticsearchRepository<Certificate, String> {


    List<Certificate> findAllByQualifications_SkillIn(Collection<String> skill);

    List<Certificate> findAllByQualifications_LevelIn(Collection<SkillLevel> level);

    List<Certificate> findAllByQualifications_SkillInAndQualifications_LevelIn(Collection<String> skill, Collection<SkillLevel> level);

    List<Certificate> findAll();
}