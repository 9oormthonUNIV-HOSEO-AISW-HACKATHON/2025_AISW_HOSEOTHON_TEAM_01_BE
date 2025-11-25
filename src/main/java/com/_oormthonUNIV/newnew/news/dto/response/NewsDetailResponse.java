package com._oormthonUNIV.newnew.news.dto.response;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.factory.NewsFactory;

public record NewsDetailResponse(
        String title,
        String thumbnailUrl,
        String category,
        String latestTime,
        String content,
        boolean reportBlur
) {
}

