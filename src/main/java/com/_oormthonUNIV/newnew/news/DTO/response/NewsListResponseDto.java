package com._oormthonUNIV.newnew.news.DTO.response;

import com._oormthonUNIV.newnew.news.entity.News;

import java.util.List;

public record NewsListResponseDto(
        List<NewsCardDto> newsList
) {
    // 서비스/컨트롤러에서 바로 쓸 수 있는 팩토리 메서드
    public static NewsListResponseDto of(boolean reportBlur, List<News> newsList) {
        List<NewsCardDto> cards = newsList.stream()
                .map(NewsCardDto::from)   // 엔티티 하나씩 DTO로 변환
                .toList();

        return new NewsListResponseDto(cards);
    }
}

