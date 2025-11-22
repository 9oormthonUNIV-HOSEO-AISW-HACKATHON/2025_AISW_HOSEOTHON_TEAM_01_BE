package com._oormthonUNIV.newnew.ai.worker;

import com._oormthonUNIV.newnew.ai.dto.ai.AllGenerationAspectDTO;
import com._oormthonUNIV.newnew.ai.dto.ai.GenerationAspectDTO;
import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;
import com._oormthonUNIV.newnew.ai.entity.AiNewsReport;
import com._oormthonUNIV.newnew.ai.factory.AiFactory;
import com._oormthonUNIV.newnew.ai.service.AiGenerationSurveyStatisticsService;
import com._oormthonUNIV.newnew.ai.service.AiNewsReportService;
import com._oormthonUNIV.newnew.global.messageQueue.task.SurveyStatisticsTask;
import com._oormthonUNIV.newnew.global.properties.OpenApiProperties;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.service.NewsService;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.survey.service.SurveyService;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
public class ApiWorker {

    private final BlockingQueue<SurveyStatisticsTask> queue;
    private final OpenAIClient client;
    private final OpenApiProperties openApiProperties;
    private final NewsService newsService;
    private final AiGenerationSurveyStatisticsService aiGenerationSurveyStatisticsService;
    private final AiNewsReportService aiNewsReportService;
    private final PromptProvider promptProvider;
    private final SurveyService surveyService;
    private final ObjectMapper mapper;

    public ApiWorker(
            BlockingQueue<SurveyStatisticsTask> queue,
            OpenApiProperties properties,
            NewsService newsService,
            AiGenerationSurveyStatisticsService aiGenerationSurveyStatisticsService,
            AiNewsReportService aiNewsReportService,
            SurveyService surveyService
    ) {
        this.queue = queue;
        this.openApiProperties = properties;
        this.newsService = newsService;
        this.aiGenerationSurveyStatisticsService = aiGenerationSurveyStatisticsService;
        this.aiNewsReportService = aiNewsReportService;
        this.surveyService = surveyService;
        this.promptProvider = new PromptProvider();
        this.mapper = new ObjectMapper();
        if (openApiProperties.getOPENAI_API_KEY() != null
                && !openApiProperties.getOPENAI_API_KEY().isBlank()
        ) {
            this.client = OpenAIOkHttpClient.builder()
                    .apiKey(openApiProperties.getOPENAI_API_KEY())
                    .build();
        } else {
            // 환경변수 OPENAI_API_KEY 사용 (또는 openai.apiKey system property)
            this.client = OpenAIOkHttpClient.fromEnv();
        }
    }

    @SneakyThrows
    @PostConstruct
    public void startWorker() {
        int workerCount = 3; // 필요에 따라 조절
        for (int i = 0; i < workerCount; i++) {
            Thread workerThread = new Thread(this::processQueue, "ApiWorker-" + i);
            workerThread.setDaemon(true);
            workerThread.start();
        }
    }

    private void processQueue() {
        while (true) {
            SurveyStatisticsTask task = null;
            try {
                task = queue.take();
                News news = newsService.getById(task.getNewsId());
                AiNewsReport newsReport =
                        aiNewsReportService.findByNewsId(news.getTitle())
                                .orElse(
                                        aiNewsReportService.save(AiNewsReport.builder()
                                                .news(news)
                                                .aspectReason(null)
                                                .commonAspect(null)
                                                .build())
                                );

                List<AiGenerationSurveyStatistics> statisticsList =
                        aiGenerationSurveyStatisticsService.getByAiNewsReportId(newsReport.getId());

                List<SurveyAnswer> newsGenerationSurveyList =
                        surveyService.getByNewsIdAndGeneration(news.getTitle(), task.getGeneration());


                // gpt응답 내역 처리 로직
                String response = chat(promptProvider.statisticGenerationAddData(
                        newsGenerationSurveyList, task.getGeneration(), news
                ));

                GenerationAspectDTO dto = mapper.readValue(response, GenerationAspectDTO.class);// 문자열 → Enum 변환
                UserGeneration generationEnum = getGeneration(dto.getGeneration());
                AiGenerationSurveyStatistics aiGenerationSurveyStatistics
                        = AiFactory.toAiGenerationSurveyStatistics(newsReport, generationEnum, dto);
                aiGenerationSurveyStatisticsService.save(aiGenerationSurveyStatistics);

                // 공통 통계도 같이 처리
                if (!(statisticsList.isEmpty() ||
                        (statisticsList.size() == 1 && statisticsList.getFirst().getGeneration() == task.getGeneration()))) {
                    // 공통 통계 처리
                    String secResponse = chat(
                            promptProvider.statisticAllGenerationAddData(news,statisticsList)
                    );
                    AllGenerationAspectDTO dto2 = mapper.readValue(secResponse, AllGenerationAspectDTO.class);// 문자열 → Enum 변환
                    newsReport.update(dto2);
                    aiNewsReportService.save(newsReport);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); // 반드시 필요
                return; // 또는 break — 스레드를 종료

            } catch (Exception e) {
                if (task == null || task.getReTryCount() >= 3) {
                    log.error("[ApiWorker] Error while processing task: {}", task, e);
                } else {
                    task.incReTryCount();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    queue.offer(task);
                }
            }
        }
    }


    public String chat(String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_4_1_MINI) // 원하는 모델로 변경
                .build();

        ChatCompletion comp = client.chat().completions().create(params);
        // 첫번째 choice의 메시지 텍스트를 리턴

        if (comp == null || comp.choices() == null || comp.choices().isEmpty()) {
            throw new IllegalStateException("OpenAI 응답이 비어있습니다.");
        }

        // 보통 첫 번째 선택지를 사용
        String first = String.valueOf(comp.choices().getFirst().message().content());
        return first == null ? "" : first.strip();

    }

    private UserGeneration getGeneration(String generation) {
        try {
            return UserGeneration.valueOf(generation);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown generation: " +generation, e);
        }
    }

}