package com._oormthonUNIV.newnew.ai.repository;

import com._oormthonUNIV.newnew.ai.entity.AiNewsReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiNewsReportRepository extends JpaRepository<AiNewsReport, Long> {
    Optional<AiNewsReport> findByNewsId(Long newsId);
}
