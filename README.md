NewNew 백엔드

프로젝트 개요
- AI 기반 뉴스 요약/설문/리포트와 JWT 인증을 제공하는 Spring Boot 3 애플리케이션입니다.
- 주요 도메인: 인증(Security), 뉴스(News), 설문(Survey), AI 통계/리포트(AI), 전역 공통(Global).
- API 문서: springdoc-openapi 기반 Swagger UI(/swagger-ui/index.html).

기술 스택
- Java 21, Spring Boot 3.5.8
- Spring Data JPA, Spring Security, Validation
- DB: MySQL 8.x (Docker-Compose 제공)
- OpenAPI 문서: springdoc-openapi-starter-webmvc-ui
- JWT: jjwt 0.11.5
- HTML 파싱: jsoup 1.21.2
- OpenAI SDK: com.openai:openai-java:4.8.0
- 빌드: Gradle (gradlew)

시스템 아키텍처 개요
- 레이어드 구조: Controller → Facade → Service → Repository → Entity/DTO/Factory
- 전역 모듈(Global): 예외 표준화, OpenAPI 설정, 공통 유틸, AOP, 로컬 캐시, 메시지 큐
- 외부 연동: OpenAI(Java SDK), MySQL, Swagger UI

패키지 구조 요약(핵심 클래스만 발췌)
- com._oormthonUNIV.newnew.global
  - config.OpenApiConfig: OpenAPI 메타/서버 및 JWT 보안 스키마 정의
  - exception: GlobalBaseException, GlobalErrorCode, GlobalExceptionHandler, GlobalExceptionResponse
  - util: TimeUtil, LongIdEntity
  - aop.DevMainLoggingAspect, cache.LocalCacheService
  - messageQueue: MessageQueueConfig, task.SurveyStatisticsTask
- com._oormthonUNIV.newnew.security
  - config.SecurityConfig: JWT 필터/인가 경로 설정
    - 허용: /api/v1/auth/**, /public/**, /swagger-ui/**, /swagger-ui.html, /v3/api-docs/**
    - ADMIN 전용: /admin/**
    - 그 외: 인증 필요
  - filter.JwtAuthenticationFilter: Authorization: Bearer <token> 검증 및 SecurityContext 주입
  - controller.SecurityController: 로그인/로그아웃/회원가입/토큰 재발급
  - facade/service/repository: RefreshToken, 액세스토큰 블랙리스트 캐시 관리
  - exception: AccessTokenBlackListException, RefreshTokenException, JwtUtilException 등
- com._oormthonUNIV.newnew.news
  - entity.News(+ NewsContent/NewsImg/NewsCategory)
  - repository.NewsRepository: 조회수 증가, 커서 기반 조회(id), Top2 조회
  - service.NewsServiceImpl: 페이지/커서 목록, 상세(조회수 증가), 랭킹
  - facade.NewsFacade, controller.NewsController, dto.response, factory.NewsFactory
  - scheduler.NaverNewsScheduler/NaverNewsCrawler: 외부 뉴스 수집(크롤링)
  - exception: NewsErrorCode/NewsException
- com._oormthonUNIV.newnew.survey
  - controller.SurveyController: 설문 문항/저장/통계 API
  - service/impl, repository.SurveyAnswerRepository, entity.SurveyAnswer, factory
  - exception: SurveyAnswerErrorCode/SurveyAnswerException
- com._oormthonUNIV.newnew.ai
  - service.impl.AiGenerationSurveyStatisticsServiceImpl, AiNewsReportServiceImpl
  - repository: AiGenerationSurveyStatisticsRepository, AiNewsReportRepository
  - worker.ApiWorker, PromptProvider
  - entity: AiGenerationSurveyStatistics, AiNewsReport

동작 플로우
- 인증(JWT)
  - POST /api/v1/auth/login: 로그인 시 Access/Refresh 토큰 발급.
  - JwtAuthenticationFilter가 요청 헤더의 Bearer 토큰을 검증해 인증 컨텍스트를 구성.
  - POST /api/v1/auth/logout: 액세스 토큰 블랙리스트 처리 + 리프레시 토큰 무효화.
  - POST /api/v1/auth/access/reissue: 리프레시 토큰으로 액세스 토큰 재발급.
  - SecurityConfig에 정의된 허용 경로 외 API는 인증 필수.
- 뉴스
  - 목록: 페이지네이션(page/size, 기본 정렬 id DESC) 또는 커서 기반(nextNewsId < id 조건) 조회.
  - 상세: 존재 확인 후 조회수 증가 처리 후 반환.
  - 인기: 조회수 상위 2건 반환.
- 설문/리포트
  - 설문 문항 조회 → 사용자 답변 저장 → 설문 통계/뉴스 리포트 조회.
  - 일부 비동기/배치 구조(MessageQueueConfig, SurveyStatisticsTask)로 통계 생성 지원 구조 포함.
- AI 통계/리포트
  - AiGenerationSurveyStatisticsServiceImpl, AiNewsReportServiceImpl가 OpenAI 연동을 래핑(ApiWorker/PromptProvider 사용).

예외 처리 표준화
- 모든 도메인 예외는 GlobalBaseException 상속 + GlobalErrorCode(enum)로 httpStatus/errorCode/message 일관 응답.
- GlobalExceptionHandler → GlobalExceptionResponse로 변환.
- 예: 뉴스 미존재 시 NewsException(NewsErrorCode.NEWS_NOT_FOUND) → 404 일관 응답.

API 엔드포인트 요약
- 인증
  - POST /api/v1/auth/login: 로그인(토큰 반환)
  - POST /api/v1/auth/logout: 로그아웃 [JWT]
  - POST /api/v1/auth/sign-up: 회원가입(토큰 반환)
  - POST /api/v1/auth/access/reissue: 액세스 토큰 재발급
- 뉴스 [JWT]
  - GET /api/v1/News?page=&size=&nextNewsId=: 뉴스 목록(페이지/커서)
  - GET /api/v1/News/{newsId}: 뉴스 상세
  - GET /api/v1/News/viewCount: 인기 뉴스 Top2
  - GET /api/v1/News/survey?page=&size=: 사용자가 설문에 참여한 뉴스 목록
- 설문 [JWT]
  - GET /api/v1/survey: 설문 문항 조회
  - POST /api/v1/survey: 설문 답변 저장
  - GET /api/v1/survey/statistics/{newsId}: 설문 통계/리포트 조회

데이터 모델(발췌)
- Users: 권한(UserRole), 세대(UserGeneration) 포함 사용자
- RefreshToken: 사용자별 리프레시 토큰 저장
- News: title, content, author, category(Enum), news_created_at(LocalDateTime), viewCount, thumbnailUrl
- SurveyAnswer: 사용자-뉴스별 설문 응답
- AiGenerationSurveyStatistics: 설문 통계 집계 결과
- AiNewsReport: AI 생성 리포트 결과

환경 설정(application.yaml) 요약
- DB: jdbc:mysql://127.0.0.1:3306/newnew, username=root, password=root
  - JPA: ddl-auto=create(개발용), show-sql=true
- JWT
  - issuer, key.access-key, key.refresh-key, ttl.access(분), ttl.refresh(일)
- OpenAI API 키
  - spring 속성: openai.api.key
  - ${OPENAI-API-KEY:} 환경변수도 참조(둘 중 하나로 주입 가능)

로컬 실행 가이드
1) 의존성 설치/빌드
   - Windows: gradlew.bat build
   - macOS/Linux: ./gradlew build
2) DB 실행
   - Docker Desktop 실행 후 루트의 Docker-Compose.yaml 사용
   - 명령: docker compose up -d
   - MySQL: 127.0.0.1:3306, DB=newnew, root/root
3) 애플리케이션 실행
   - Windows: gradlew.bat bootRun 또는 java -jar build/libs/*.jar
   - 기본 포트: 8080
4) API 문서
   - http://localhost:8080/swagger-ui/index.html
   - JWT 요구 엔드포인트는 Authorize 버튼으로 Bearer <액세스토큰> 입력

개발 시 유의 사항
- 인증 정책: SecurityController의 인증 API 등 허용 경로 외 대부분은 JWT 필요.
- 정렬/시간: 뉴스 기본 정렬은 id DESC, 생성 시각 필드는 news_created_at.
- 예외 응답: GlobalExceptionResponse 포맷으로 errorCode/httpStatus/message 일관 제공.
- 초기 데이터: src/main/resources/data.sql 존재 시 ddl-auto=create 환경에서 초기 데이터 로드될 수 있음.

라이선스
- 현재 명시된 라이선스가 없습니다. 외부 배포 시 라이선스 명시 권장.
