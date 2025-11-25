package com._oormthonUNIV.newnew.news.repository;

import com._oormthonUNIV.newnew.news.entity.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News,Long> {

    @Modifying // 조회수 + 1
    @Query("update News n set n.viewCount = n.viewCount + 1 where n.id = :id")
    void increaseViewCount(@Param("id") Long id);

    // 커서 기반 조회: 특정 id보다 작은 뉴스들을 id 내림차순으로 조회 (페이지 크기 제한)
    List<News> findAllByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

    // viewCount 내림차순으로 상위 2개
    List<News> findTop2ByOrderByViewCountDesc();
    // 인기순 조회용
//    List<News> findTop10ByOrderByViewCountDescNews_created_atDesc();
}
