package com._oormthonUNIV.newnew.news.DTO.response;

import com._oormthonUNIV.newnew.news.entity.News;

import java.time.Duration;
import java.time.LocalDateTime;

public record NewsCardDto(
        Long newsId,
        String title,
        String thumbnailUrl,
        String category,
        String latestTime
) {
    public static NewsCardDto from(News news) {
        return new NewsCardDto(
                news.getId(),                        // LongIdEntity 에서 물려받은 PK
                news.getTitle(),
                null,                                 // TODO: 썸네일 필드 생기면 news.getThumbnailUrl()
                news.getCategory() != null ? news.getCategory().name() : null,
                toLatestTime(news.getNews_created_at())
        );
    }

    private static String toLatestTime(LocalDateTime createdAt) {
        if (createdAt == null) return null;

        Duration d = Duration.between(createdAt, LocalDateTime.now());

        long minutes = d.toMinutes();
        long hours = d.toHours();
        long days = d.toDays();

        if (minutes < 1) return "방금 전";
        if (hours < 1) return minutes + "분 전";
        if (days < 1) return hours + "시간 전";
        return days + "일 전";
    }
}
