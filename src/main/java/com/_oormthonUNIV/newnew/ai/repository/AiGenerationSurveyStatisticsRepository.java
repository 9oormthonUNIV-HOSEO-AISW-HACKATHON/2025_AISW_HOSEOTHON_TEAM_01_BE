package com._oormthonUNIV.newnew.ai.repository;

import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiGenerationSurveyStatisticsRepository extends JpaRepository<AiGenerationSurveyStatistics, Long> {
    List<AiGenerationSurveyStatistics> findByReport_Id(Long reportId);
}
