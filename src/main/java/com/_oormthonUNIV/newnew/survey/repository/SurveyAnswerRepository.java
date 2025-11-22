package com._oormthonUNIV.newnew.survey.repository;

import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

    List<SurveyAnswer> findByNewsIdAndGeneration(Long newsId, UserGeneration generation);

}
