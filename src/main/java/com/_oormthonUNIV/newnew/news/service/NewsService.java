package com._oormthonUNIV.newnew.news.service;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.user.entity.Users;

import java.util.List;

public interface NewsService {
    News getById(Long newsId);

    List<News> getPageableNews(Integer page, Integer size, Long nextNewsId);

    News getNewsDetail(Long newsId);

    List<News> getRanking();
}
