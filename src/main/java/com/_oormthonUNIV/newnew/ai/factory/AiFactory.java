package com._oormthonUNIV.newnew.ai.factory;

import com._oormthonUNIV.newnew.ai.dto.ai.GenerationAspectDTO;
import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;
import com._oormthonUNIV.newnew.ai.entity.AiNewsReport;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;

public class AiFactory {

    public static AiGenerationSurveyStatistics toAiGenerationSurveyStatistics(AiNewsReport report, UserGeneration generation, GenerationAspectDTO dto) {
        return  AiGenerationSurveyStatistics.builder()
                    .report(report)
                    .generation(generation)
                    .firstAspect(dto.getFirstAspect())
                    .firstAspectReason(dto.getFirstAspectReason())
                    .secondAspect(dto.getSecondAspect())
                    .secondAspectReason(dto.getSecondAspectReason())
                    .thirdAspect(dto.getThirdAspect())
                    .thirdAspectReason(dto.getThirdAspectReason())
                    .build();
    }
}
