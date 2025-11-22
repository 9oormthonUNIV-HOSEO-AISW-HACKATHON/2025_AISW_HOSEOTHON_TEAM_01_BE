package com._oormthonUNIV.newnew.news.service.impl;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.service.NewsService;
import com._oormthonUNIV.newnew.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    @Override
    public News getById(String newsId) {
        return null;
    }

    @Override
    public List<News> getUserSurveiedNews(Users user) {
        return List.of();
    }
}
