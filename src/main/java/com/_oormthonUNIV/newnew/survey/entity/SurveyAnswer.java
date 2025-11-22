package com._oormthonUNIV.newnew.survey.entity;

import com._oormthonUNIV.newnew.global.util.LongIdEntity;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyAnswer extends LongIdEntity {

    @Column(nullable = false)
    private Long question_id;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGeneration generation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    public boolean isOwner(Users user) {
        return this.user.getIdentificationId().equals(user.getIdentificationId());
    }

}
