package com._oormthonUNIV.newnew.news.dto.response;

public record NewsCardResponse(
        Long newsId,
        String title,
        String thumbnailUrl,
        String category,
        String latestTime
) {
}
