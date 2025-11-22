package com._oormthonUNIV.newnew.news.service.impl;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    @Override
    public News getById(String newsId) {
        return null;
    }
}
