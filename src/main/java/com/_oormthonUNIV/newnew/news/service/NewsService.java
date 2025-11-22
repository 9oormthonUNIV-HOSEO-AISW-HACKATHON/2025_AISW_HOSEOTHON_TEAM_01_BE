package com._oormthonUNIV.newnew.news.service;

import com._oormthonUNIV.newnew.news.DTO.response.NewsListResponseDto;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.user.entity.Users;

import java.util.List;
import java.util.UUID;

public interface NewsService {
    News getById(Long newsId);

    List<News> getUserSurveiedNews(Users user);

    boolean isReportBlur(Long userId);

    NewsListResponseDto getNewsList();

    News getNewsDetail(Long newsId);
}
