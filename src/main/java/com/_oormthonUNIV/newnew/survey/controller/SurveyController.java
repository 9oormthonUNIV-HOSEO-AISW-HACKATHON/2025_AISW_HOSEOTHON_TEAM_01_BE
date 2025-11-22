package com._oormthonUNIV.newnew.survey.controller;

import com._oormthonUNIV.newnew.security.entity.UserDetailImpl;
import com._oormthonUNIV.newnew.survey.dto.request.UserSurveySaveRequest;
import com._oormthonUNIV.newnew.survey.dto.response.NewsSurveyResponse;
import com._oormthonUNIV.newnew.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping("/survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 목록 조회 성공")
    })
    @Operation(summary = "뉴스 설문 질문 조회", description = "뉴스 설문 질문 목록을 조회합니다. 모든 사용자에게 동일한 고정 질문 반환")
    public List<NewsSurveyResponse> getSurveys() {
        return surveyService.getSurveys();
    }

    @PostMapping("/survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @Operation(summary = "뉴스 설문 답변 저장", description = "사용자의 설문 답변을 저장합니다")
    public void saveSurvey(
            @RequestBody UserSurveySaveRequest request,
            @AuthenticationPrincipal UserDetailImpl userDetails
    ) {
        surveyService.saveUserAnswer(request, userDetails.getUser());
    }

}
