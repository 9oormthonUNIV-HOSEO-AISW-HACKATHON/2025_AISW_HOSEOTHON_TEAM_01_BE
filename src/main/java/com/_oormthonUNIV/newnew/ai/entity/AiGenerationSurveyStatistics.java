package com._oormthonUNIV.newnew.ai.entity;

import com._oormthonUNIV.newnew.global.util.LongIdEntity;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiGenerationSurveyStatistics extends LongIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_news_report_id", nullable = false)
    private AiNewsReport report;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGeneration generation;

    @Column(columnDefinition = "TEXT")
    private String firstAspect;

    @Column(columnDefinition = "TEXT")
    private String firstAspectReason;

    @Column(columnDefinition = "TEXT")
    private String secondAspect;

    @Column(columnDefinition = "TEXT")
    private String secondAspectReason;

    @Column(columnDefinition = "TEXT")
    private String thirdAspect;

    @Column(columnDefinition = "TEXT")
    private String thirdAspectReason;

}
