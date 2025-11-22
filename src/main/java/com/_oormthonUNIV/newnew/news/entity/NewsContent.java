package com._oormthonUNIV.newnew.news.entity;

import com._oormthonUNIV.newnew.global.util.LongIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class NewsContent extends LongIdEntity {
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

}
