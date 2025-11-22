package com._oormthonUNIV.newnew.ai.service.impl;

import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;
import com._oormthonUNIV.newnew.ai.repository.AiGenerationSurveyStatisticsRepository;
import com._oormthonUNIV.newnew.ai.service.AiGenerationSurveyStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiGenerationSurveyStatisticsServiceImpl implements AiGenerationSurveyStatisticsService {

    private final AiGenerationSurveyStatisticsRepository repository;

    @Override
    public List<AiGenerationSurveyStatistics> getByAiNewsReportId(Long aiNewsReportId) {
        return repository.findByReport_Id(aiNewsReportId);
    }

    @Override
    public void save(AiGenerationSurveyStatistics aiGenerationSurveyStatistics) {
        repository.save(aiGenerationSurveyStatistics);
    }
}
