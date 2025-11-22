package com._oormthonUNIV.newnew.ai.service;

import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;

import java.util.List;

public interface AiGenerationSurveyStatisticsService {
    List<AiGenerationSurveyStatistics> getByAiNewsReportId(Long id);

    void save(AiGenerationSurveyStatistics aiGenerationSurveyStatistics);
}
