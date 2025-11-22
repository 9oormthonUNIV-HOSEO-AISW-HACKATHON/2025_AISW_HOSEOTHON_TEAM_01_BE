package com._oormthonUNIV.newnew.news.repository;

import com._oormthonUNIV.newnew.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NewsCrawlerRepository extends JpaRepository<News,Long> {
    @Override
    <S extends News> List<S> saveAll(Iterable<S> entities);

    @Override
    <S extends News> S save(S entity);

    @Modifying // 조회수 + 1
    @Query("update News n set n.viewCount = n.viewCount + 1 where n.id = :id")
    void increaseViewCount(@Param("id") Long id);

    // 인기순 조회용
//    List<News> findTop10ByOrderByViewCountDescNews_created_atDesc();
}
