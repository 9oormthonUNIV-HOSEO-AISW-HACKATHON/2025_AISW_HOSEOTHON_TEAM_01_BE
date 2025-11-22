package com._oormthonUNIV.newnew.ai.worker;

import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import lombok.Getter;

import java.util.List;

@Getter
public class PromptProvider {
    private final String statisticGeneration =
            """
            당신의 역할은 “세대별 뉴스 설문 분석기”입니다.
            아래 입력으로 제공되는 뉴스의 본문과, 특정 세대의 설문 응답 데이터를 기반으로
            해당 세대가 공통적으로 가지는 ‘주요 관점 TOP 3’와
            각 관점의 이유를 하나의 문장으로 요약하여 도출하세요.
    
            ### 분석 방식 규칙
            1. Q1~Q5 설문 응답들을 모두 활용하여 세대의 시각적 특징, 문제 인식, 원인 판단, 해결 방향을 분석합니다.
            2. “세대 내 다수 의견 경향성”을 기반으로 관점을 도출하세요.
            3. 관점은 최대한 구체적이며 중복되지 않아야 합니다.
            4. 이유는 하나의 문장으로 작성합니다.
            5. 결과는 반드시 JSON 형식으로 출력합니다.
            6. JSON 구조는 개발자가 DB에 그대로 저장할 수 있는 형태여야 합니다.
    
            ### 출력 JSON 포맷 (엄격 준수)
            {
              "generation": SILENT, BABY_BOOMER, GEN_X, MILLENNIAL, GEN_Z, GEN_ALPHA 중 제공된 데이터에 맞게 설정,
              "firstAspect": "string",
              "firstAspectReason": "string",
              "secondAspect": "string",
              "secondAspectReason": "string",
              "thirdAspect": "string",
              "thirdAspectReason": "string"
            }
    
            ### 입력으로 제공되는 데이터 설명
            - generation: 분석할 세대 : SILENT, BABY_BOOMER, GEN_X, MILLENNIAL, GEN_Z, GEN_ALPHA 중 1개
            - news : 뉴스의 본문
            - answers: 설문 응답 데이터 리스트
              Q1: 뉴스 요약
              Q2: 가장 큰 문제
              Q3: 원인 판단
              Q4: 해결책
              Q5: 전반적 인상 점수
            
            ### 제공 데이터
            """;

    private final String statisticAllGenerations =
            """
            당신의 역할은 “세대 간 공통 관점 분석기”입니다.
    
            아래 입력으로 제공되는 모든 세대별 설문 분석 결과를 기반으로
            “모든 세대가 공통적으로 동의하거나 공통적으로 문제의식을 가진 관점”을 한 개 도출하고,
            그 이유를 하나의 문장으로로 요약하세요.
    
            ### 분석 방식 규칙
            1. 각 세대의 top 3 관점들을 비교하여 의미적으로 가장 강한 중복을 찾습니다.
            2. 공통 관점은 중복도가 가장 높은 하나의 문장으로 구성합니다.
            3. 관점의 이유는 핵심 키워드를 콤마로 구분하여 간결하게 작성합니다.
            4. 결과는 반드시 아래 JSON 형식으로 출력합니다.
            5. JSON 구조는 AiNewsReport 엔티티에 저장 가능한 형태여야 합니다.
            
            ### AiNewsReport 구조(java)
            public AiNewsReport{
                private String commonAspect;
                private String aspectReason;
            }
    
            ### 출력 JSON 포맷
            {
              "commonAspect": "string",
              "aspectReason": "string"
            }
    
            ### 입력 데이터 설명
            - news : 뉴스의 본문
            - generation : 세대
                - aspect1 : 해당세대 관점 1번
                - aspect1Reason : 관점 1번 이유
                - aspect2 : 해당세대 관점 2번
                - aspect2Reason : 관점 2번 이유
                - aspect3 : 해당세대 관점 3번
                - aspect3Reason : 관점 3번 이유
            
            ### 데이터
            
            """;

    public String statisticAllGenerationAddData(
        News news,List<AiGenerationSurveyStatistics> aiGenerationSurveyStatisticList
    ){
        StringBuilder common = new StringBuilder();
        common.append("news : ").append(news.getContent()).append("\n");
        for (AiGenerationSurveyStatistics ai : aiGenerationSurveyStatisticList) {
            common.append("- generation : ").append(ai.getGeneration()).append("\n");
            common.append("\t").append("- aspect1 : ").append(ai.getFirstAspect()).append("\n");
            common.append("\t").append("- aspect1Reason : ").append(ai.getFirstAspectReason()).append("\n");
            common.append("\t").append("- aspect2 : ").append(ai.getSecondAspect()).append("\n");
            common.append("\t").append("- aspect2Reason : ").append(ai.getSecondAspectReason()).append("\n");
            common.append("\t").append("- aspect3 : ").append(ai.getThirdAspect()).append("\n");
            common.append("\t").append("- aspect3Reason : ").append(ai.getThirdAspectReason()).append("\n");
        }
        return statisticAllGenerations + common.toString();
    }

    public String statisticGenerationAddData(
            List<SurveyAnswer> answerList,
            UserGeneration generation,
            News news
    ) {
        StringBuilder common = new StringBuilder();

        common.append("- generation: ").append(generation).append("\n");
        common.append("- news: ").append(news.getContent()).append("\n");
        common.append("- answers:\n");

        for (SurveyAnswer answer : answerList) {
            short q = getQuestionNum(answer.getQuestion_id());
            common.append("  Q").append(q).append(": ").append(answer.getAnswer()).append("\n");
        }

        // AI 프롬프트와 설문 데이터를 연결
        return statisticGeneration + "\n" + common.toString();
    }


    private short getQuestionNum(long a) {
        if (a >= 1 && a <= 5) return (short) a;
        throw new IllegalArgumentException("Unknown question_id: " + a);
    }



}
