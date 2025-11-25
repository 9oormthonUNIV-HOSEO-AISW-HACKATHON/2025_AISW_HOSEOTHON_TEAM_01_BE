package com._oormthonUNIV.newnew.news.factory;

import com._oormthonUNIV.newnew.global.util.TimeUtil;
import com._oormthonUNIV.newnew.news.dto.response.NewsCardResponse;
import com._oormthonUNIV.newnew.news.dto.response.NewsDetailResponse;
import com._oormthonUNIV.newnew.news.dto.response.NewsListResponse;
import com._oormthonUNIV.newnew.news.entity.News;

import java.util.List;

public class NewsFactory {
    public static NewsListResponse toNewsListResponse(List<News> newsList) {
        List<NewsCardResponse> cards = newsList.stream()
                .map(NewsFactory::toNewsCardResponse)   // 엔티티 하나씩 DTO로 변환
                .toList();

        return new NewsListResponse(cards);
    }

    public static NewsCardResponse toNewsCardResponse(News news) {
        return new NewsCardResponse(
                news.getId(),                        // LongIdEntity 에서 물려받은 PK
                news.getTitle(),
                news.getThumbnailUrl(),
                news.getCategory() != null ? news.getCategory().name() : null,
                TimeUtil.toLatestTime(news.getNews_created_at())
        );
    }

    public static NewsDetailResponse toNewsDetailResponse(News news, boolean reportBlur) {
        String latestTime = TimeUtil.toRelativeTime(news.getNews_created_at());

        return new NewsDetailResponse(
                news.getTitle(),
                news.getThumbnailUrl(),
                news.getCategory() != null ? news.getCategory().name() : null,
                latestTime,
                news.getContent(),
                reportBlur
        );
    }
}
