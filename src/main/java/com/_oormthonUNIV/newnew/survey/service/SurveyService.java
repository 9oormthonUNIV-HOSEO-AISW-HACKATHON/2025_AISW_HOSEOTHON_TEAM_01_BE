package com._oormthonUNIV.newnew.survey.service;

import com._oormthonUNIV.newnew.survey.dto.request.UserSurveySaveRequest;
import com._oormthonUNIV.newnew.survey.dto.response.NewsSurveyResponse;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;

import java.util.List;

public interface SurveyService {

    List<NewsSurveyResponse> getSurveys();

    void saveUserAnswer(UserSurveySaveRequest request, Users user);

    List<SurveyAnswer> getByNewsIdAndGeneration(String newsId, UserGeneration generation);
}
