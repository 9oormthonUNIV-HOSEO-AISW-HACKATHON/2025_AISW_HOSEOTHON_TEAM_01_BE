package com._oormthonUNIV.newnew.news.facade;

import com._oormthonUNIV.newnew.news.dto.response.NewsDetailResponse;
import com._oormthonUNIV.newnew.news.dto.response.NewsListResponse;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.factory.NewsFactory;
import com._oormthonUNIV.newnew.news.service.NewsService;
import com._oormthonUNIV.newnew.survey.service.SurveyService;
import com._oormthonUNIV.newnew.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsFacade {

    private final NewsService newsService;
    private final SurveyService surveyService;

    public NewsListResponse getNewsList(Integer page, Integer size, Long nextNewsId) {
        List<News> newsList = newsService.getPageableNews(page, size, nextNewsId);
        return NewsFactory.toNewsListResponse(newsList);
    }

    public NewsDetailResponse getNewsDetail(Long newsId, Users user) {
        News news = newsService.getNewsDetail(newsId);
        boolean isSurveyed = surveyService.isExistSurvey(news.getId(), user);
        return NewsFactory.toNewsDetailResponse(news, isSurveyed);
    }

    public NewsListResponse getTop2News() {
        List<News> news = newsService.getRanking();
        return NewsFactory.toNewsListResponse(news);
    }

    public NewsListResponse getUserSurveyedNews(Users user,Integer page, Integer size) {
        List<News> userSurveyedNewsList =
                surveyService.getSurveyedNews(user.getId(),page, size);

        return NewsFactory.toNewsListResponse(userSurveyedNewsList);
    }
}
