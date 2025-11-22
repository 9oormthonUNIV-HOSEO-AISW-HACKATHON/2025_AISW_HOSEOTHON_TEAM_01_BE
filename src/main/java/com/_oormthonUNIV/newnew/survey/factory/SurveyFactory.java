package com._oormthonUNIV.newnew.survey.factory;

import com._oormthonUNIV.newnew.global.messageQueue.task.SurveyStatisticsTask;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.survey.dto.request.UserSurveySaveRequest;
import com._oormthonUNIV.newnew.survey.dto.response.NewsSurveyResponse;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.user.entity.Users;

import java.util.ArrayList;
import java.util.List;

public class SurveyFactory {

    public static List<NewsSurveyResponse> getSurveys() {
        return List.of(
                new NewsSurveyResponse(1, "이 뉴스를 한 문장으로 요약하면 어떻게 생각하나요?"),
                new NewsSurveyResponse(2, "이 뉴스에서 가장 중요한 문제는 무엇이라고 보나요?"),
                new NewsSurveyResponse(3, "이 문제가 발생한 가장 큰 원인은 무엇이라 생각하나요?"),
                new NewsSurveyResponse(4, "당신이라면 어떤 해결책을 가장 먼저 시도하겠습니까?"),
                new NewsSurveyResponse(5, "이 뉴스에 대한 전반적 인상을 0~100으로 점수로 표현해주세요.")
        );
    }

    public static List<SurveyAnswer> toSurveyAnswers(News news, UserSurveySaveRequest request, Users user) {
        List<String> answers = List.of(
                request.getQ1Answer(),
                request.getQ2Answer(),
                request.getQ3Answer(),
                request.getQ4Answer(),
                request.getQ5Answer()
        );
        List<SurveyAnswer> surveyAnswers = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            surveyAnswers.add(SurveyAnswer.builder()
                    .news(news)
                    .question_id((long) (i + 1))
                    .answer(answers.get(i))
                    .generation(user.getGeneration())
                    .user(user)
                    .build()
            );
        }
        return surveyAnswers;
    }

    public static SurveyStatisticsTask toSurveyStatisticsTask(News news, Users user) {
        return SurveyStatisticsTask.builder()
                .newsId(news.getTitle())
                .generation(user.getGeneration())
                .reTryCount((short) 0)
                .build();
    }


}
