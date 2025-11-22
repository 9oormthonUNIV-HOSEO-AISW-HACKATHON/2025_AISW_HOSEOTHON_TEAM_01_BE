package com._oormthonUNIV.newnew.news.DTO.response;

import com._oormthonUNIV.newnew.news.entity.News;

import java.time.Duration;
import java.time.LocalDateTime;

public record NewsDetailResponseDto(
        String title,
        String thumbnailUrl,
        String category,
        String latestTime,
        String content,
        boolean reportBlur
) {
    public static NewsDetailResponseDto of(News news, boolean reportBlur) {
        String latestTime = TimeUtil.toRelativeTime(news.getNews_created_at());

        return new NewsDetailResponseDto(
                news.getTitle(),
                news.getThumbnailUrl(),
                news.getCategory() != null ? news.getCategory().name() : null,
                latestTime,
                news.getContent(),
                reportBlur
        );
    }
}

// 같은 파일 아래에 유틸 클래스 같이 정의
final class TimeUtil {

    private TimeUtil() {
    }

    static String toRelativeTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long minutes = duration.toMinutes();
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";

        long hours = duration.toHours();
        if (hours < 24) return hours + "시간 전";

        long days = duration.toDays();
        if (days < 7) return days + "일 전";

        if (days < 30) return (days / 7) + "주 전";
        if (days < 365) return (days / 30) + "개월 전";

        return (days / 365) + "년 전";
    }
}
