package com._oormthonUNIV.newnew.survey.dto.response;

import com._oormthonUNIV.newnew.ai.dto.ai.GenerationAspectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsReportResponse {
    private Long newsId;
    private String commonAspect;
    private String aspectReason;
    private List<GenerationAspectDTO> generationAspectList;
}
