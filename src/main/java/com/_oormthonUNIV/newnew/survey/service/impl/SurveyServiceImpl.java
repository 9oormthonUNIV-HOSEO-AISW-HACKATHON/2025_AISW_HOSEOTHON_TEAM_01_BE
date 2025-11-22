package com._oormthonUNIV.newnew.survey.service.impl;

import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;
import com._oormthonUNIV.newnew.ai.entity.AiNewsReport;
import com._oormthonUNIV.newnew.ai.service.AiGenerationSurveyStatisticsService;
import com._oormthonUNIV.newnew.ai.service.AiNewsReportService;
import com._oormthonUNIV.newnew.global.messageQueue.task.SurveyStatisticsTask;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.service.NewsService;
import com._oormthonUNIV.newnew.survey.dto.request.UserSurveySaveRequest;
import com._oormthonUNIV.newnew.survey.dto.response.NewsReportResponse;
import com._oormthonUNIV.newnew.survey.dto.response.NewsSurveyResponse;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.survey.exception.SurveyAnswerErrorCode;
import com._oormthonUNIV.newnew.survey.exception.SurveyAnswerException;
import com._oormthonUNIV.newnew.survey.factory.SurveyFactory;
import com._oormthonUNIV.newnew.survey.repository.SurveyAnswerRepository;
import com._oormthonUNIV.newnew.survey.service.SurveyService;
import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private final BlockingQueue<SurveyStatisticsTask> queue;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final NewsService newsService;
    private final AiNewsReportService reportService;
    private final AiGenerationSurveyStatisticsService surveyStatisticsService;

    @Override
    public List<NewsSurveyResponse> getSurveys() {
        return SurveyFactory.getSurveys();
    }

    @Override
    public void saveUserAnswer(UserSurveySaveRequest request, Users user) {
        News news = newsService.getById(request.getNewsId());
        List<SurveyAnswer> answerList =
                surveyAnswerRepository.findByNewsIdAndUserId(news.getId(), user.getId());
        if(!answerList.isEmpty()){
            throw new SurveyAnswerException(SurveyAnswerErrorCode.SURVEY_ANSWER_DUPLICATED);
        }
        answerList = SurveyFactory.toSurveyAnswers(news, request, user);
        SurveyStatisticsTask task = SurveyFactory.toSurveyStatisticsTask(news, user);
        surveyAnswerRepository.saveAll(answerList);
        queue.offer(task);
    }

    @Override
    public List<SurveyAnswer> getByNewsIdAndGeneration(Long newsId, UserGeneration generation) {
        return surveyAnswerRepository.findByNewsIdAndGeneration(newsId, generation);
    }

    @Override
    public NewsReportResponse getNewsReport(Long newsId, Users user) {
        List<SurveyAnswer> answerList =
                surveyAnswerRepository.findByNewsIdAndUserId(newsId, user.getId());
        if(answerList.isEmpty()){
            throw new SurveyAnswerException(SurveyAnswerErrorCode.SURVEY_BAD_ACCESS);
        }

        AiNewsReport report = reportService.findByNewsId(newsId)
                .orElseThrow( () -> new SurveyAnswerException(SurveyAnswerErrorCode.AI_REPORT_NOT_FOUND) );
        List<AiGenerationSurveyStatistics> statisticsList =
                surveyStatisticsService.getByAiNewsReportId(report.getId());

        return SurveyFactory.toNewsReportResponse(newsId, report, statisticsList);
    }

    @Override
    public boolean isExistSurvey(Long newsId, Users user) {
        List<SurveyAnswer> answerList =
                surveyAnswerRepository.findByNewsIdAndUserId(newsId, user.getId());
        if(answerList.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

}
