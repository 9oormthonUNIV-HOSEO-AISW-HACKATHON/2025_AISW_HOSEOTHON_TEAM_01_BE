package com._oormthonUNIV.newnew.ai.entity;

import com._oormthonUNIV.newnew.global.util.LongIdEntity;
import com._oormthonUNIV.newnew.news.entity.News;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiNewsReport extends LongIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    private String commonAspect;

    private String aspectReason;

}
