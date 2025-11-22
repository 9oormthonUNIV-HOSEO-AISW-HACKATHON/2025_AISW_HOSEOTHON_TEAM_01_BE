package com._oormthonUNIV.newnew.global.messageQueue.task;

import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyStatisticsTask {

    //설문한 뉴스 아이디
    private Long newsId;

    //설문을 마친 유저의 세대
    private UserGeneration generation;

    //api호출 재시도 횟수
    private short reTryCount;

    public void incReTryCount() {
        this.reTryCount++;
    }

}
