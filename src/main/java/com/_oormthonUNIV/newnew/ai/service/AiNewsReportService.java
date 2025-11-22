package com._oormthonUNIV.newnew.ai.service;

import com._oormthonUNIV.newnew.ai.entity.AiNewsReport;

import java.util.Optional;

public interface AiNewsReportService {
    Optional<AiNewsReport> findByNewsId(Long id);

    AiNewsReport save(AiNewsReport build);
}
