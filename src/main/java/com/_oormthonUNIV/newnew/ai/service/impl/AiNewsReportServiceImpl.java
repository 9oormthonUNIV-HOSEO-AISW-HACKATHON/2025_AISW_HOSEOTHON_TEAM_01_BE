package com._oormthonUNIV.newnew.ai.service.impl;

import com._oormthonUNIV.newnew.ai.entity.AiNewsReport;
import com._oormthonUNIV.newnew.ai.repository.AiNewsReportRepository;
import com._oormthonUNIV.newnew.ai.service.AiNewsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiNewsReportServiceImpl implements AiNewsReportService {

    private final AiNewsReportRepository repository;

    @Override
    public Optional<AiNewsReport> findByNewsId(Long newsId) {
        return repository.findById(newsId);
    }

    @Override
    public AiNewsReport save(AiNewsReport build) {
        return repository.save(build);
    }
}
