package com._oormthonUNIV.newnew.survey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 설문 응답 저장 요청 DTO")
public class UserSurveySaveRequest {

    @NotBlank(message = "뉴스id는 비어있을 수 없습니다.")
    private String newsId;

    @NotBlank(message = "질문1 답변은 비어있을 수 없습니다.")
    @Max(value = 200, message = "최대 답변 글자 수는 200자 입니다.")
    @Schema(description = "질문1 답변", maxLength = 200, example = "저는 이런 생각을 합니다.")
    private String q1Answer;

    @NotBlank(message = "질문2 답변은 비어있을 수 없습니다.")
    @Max(value = 200, message = "최대 답변 글자 수는 200자 입니다.")
    @Schema(description = "질문2 답변", maxLength = 200, example = "저는 이런 경험이 있습니다.")
    private String q2Answer;

    @NotBlank(message = "질문3 답변은 비어있을 수 없습니다.")
    @Max(value = 200, message = "최대 답변 글자 수는 200자 입니다.")
    @Schema(description = "질문3 답변", maxLength = 200, example = "저는 이렇게 생각합니다.")
    private String q3Answer;

    @NotBlank(message = "질문4 답변은 비어있을 수 없습니다.")
    @Max(value = 200, message = "최대 답변 글자 수는 200자 입니다.")
    @Schema(description = "질문4 답변", maxLength = 200, example = "제 의견은 이렇습니다.")
    private String q4Answer;

    @NotBlank(message = "질문5 답변은 비어있을 수 없습니다.")
    @Max(value = 200, message = "최대 답변 글자 수는 200자 입니다.")
    @Schema(description = "질문5 답변", maxLength = 200, example = "마지막으로 이렇게 생각합니다.")
    private String q5Answer;

}
