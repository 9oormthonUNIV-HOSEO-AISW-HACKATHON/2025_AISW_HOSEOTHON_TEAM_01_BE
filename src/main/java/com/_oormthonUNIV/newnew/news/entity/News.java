package com._oormthonUNIV.newnew.news.entity;

import com._oormthonUNIV.newnew.global.util.LongIdEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News extends LongIdEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    @Enumerated(EnumType.STRING)
    private NewsCategory category;

    @Column(nullable = false)
    private LocalDateTime news_created_at;

    @Builder.Default
    @Column(nullable = false)
    private long viewCount = 0L;   // 조회수

    private String thumbnailUrl;
}
