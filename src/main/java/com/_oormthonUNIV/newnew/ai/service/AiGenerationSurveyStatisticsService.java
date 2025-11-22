package com._oormthonUNIV.newnew.ai.service;

import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;

import java.util.List;

public interface AiGenerationSurveyStatisticsService {
    List<AiGenerationSurveyStatistics> getByNewsId(Long id);
}
