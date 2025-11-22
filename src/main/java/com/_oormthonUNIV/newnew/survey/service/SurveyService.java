package com._oormthonUNIV.newnew.survey.service;

import com._oormthonUNIV.newnew.survey.dto.request.UserSurveySaveRequest;
import com._oormthonUNIV.newnew.survey.dto.response.NewsReportResponse;
import com._oormthonUNIV.newnew.survey.dto.response.NewsSurveyResponse;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;

import java.util.List;

public interface SurveyService {

    List<NewsSurveyResponse> getSurveys();

    void saveUserAnswer(UserSurveySaveRequest request, Users user);

    List<SurveyAnswer> getByNewsIdAndGeneration(Long newsId, UserGeneration generation);

    NewsReportResponse getNewsReport(Long newsId, Users user);

    boolean isExistSurvey(Long newsId, Users user);
}
