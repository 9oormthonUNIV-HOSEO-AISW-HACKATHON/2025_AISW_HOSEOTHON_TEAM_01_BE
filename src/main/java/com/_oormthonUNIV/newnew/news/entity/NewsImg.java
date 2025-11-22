package com._oormthonUNIV.newnew.news.entity;

import com._oormthonUNIV.newnew.global.util.LongIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


public class NewsImg extends LongIdEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imgPath;

    @Column(nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;
}
