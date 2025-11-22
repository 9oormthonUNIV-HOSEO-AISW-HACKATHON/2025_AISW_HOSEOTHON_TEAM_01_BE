package com._oormthonUNIV.newnew.survey.service.impl;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.service.NewsService;
import com._oormthonUNIV.newnew.survey.dto.request.UserSurveySaveRequest;
import com._oormthonUNIV.newnew.survey.dto.response.NewsSurveyResponse;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.survey.factory.SurveyFactory;
import com._oormthonUNIV.newnew.survey.repository.SurveyAnswerRepository;
import com._oormthonUNIV.newnew.survey.service.SurveyService;
import com._oormthonUNIV.newnew.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private final SurveyAnswerRepository surveyAnswerRepository;
    private final NewsService newsService;


    @Override
    public List<NewsSurveyResponse> getSurveys() {
        return SurveyFactory.getSurveys();
    }

    @Override
    public void saveUserAnswer(UserSurveySaveRequest request, Users user) {
        News news = newsService.getById(request.getNewsId());
        List<SurveyAnswer> answerList = SurveyFactory.toSurveyAnswers(news, request, user);
        surveyAnswerRepository.saveAll(answerList);
        // -> 여기에 세대별 로직 만들어야함
    }
}
