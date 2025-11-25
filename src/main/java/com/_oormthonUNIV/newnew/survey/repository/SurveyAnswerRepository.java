package com._oormthonUNIV.newnew.survey.repository;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

    List<SurveyAnswer> findByNewsIdAndGeneration(Long newsId, UserGeneration generation);
    List<SurveyAnswer> findByNewsIdAndUserId(Long newsId, Long userId);

    @Query(
            value = "select distinct sa.news from SurveyAnswer sa where sa.user.id = :userId",
            countQuery = "select count(distinct sa.news) from SurveyAnswer sa where sa.user.id = :userId"
    )
    Page<News> findDistinctNewsByUserId(@Param("userId") Long userId, Pageable pageable);

}
