package com._oormthonUNIV.newnew.ai.worker;

import com._oormthonUNIV.newnew.ai.entity.AiGenerationSurveyStatistics;
import com._oormthonUNIV.newnew.ai.service.AiGenerationSurveyStatisticsService;
import com._oormthonUNIV.newnew.global.messageQueue.task.SurveyStatisticsTask;
import com._oormthonUNIV.newnew.global.properties.OpenApiProperties;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.service.NewsService;
import com._oormthonUNIV.newnew.survey.entity.SurveyAnswer;
import com._oormthonUNIV.newnew.survey.service.SurveyService;
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
    private final PromptProvider promptProvider;
    private final SurveyService surveyService;

    public ApiWorker(
            BlockingQueue<SurveyStatisticsTask> queue,
            OpenApiProperties properties,
            NewsService newsService,
            AiGenerationSurveyStatisticsService aiGenerationSurveyStatisticsService, SurveyService surveyService
    ) {
        this.queue = queue;
        this.openApiProperties = properties;
        this.newsService = newsService;
        this.aiGenerationSurveyStatisticsService = aiGenerationSurveyStatisticsService;
        this.surveyService = surveyService;
        this.promptProvider = new PromptProvider();
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
                List<AiGenerationSurveyStatistics> statisticsList =
                        aiGenerationSurveyStatisticsService.getByNewsId(news.getId());

                List<SurveyAnswer> newsGenerationSurveyList =
                        surveyService.getByNewsIdAndGeneration(news.getId(), task.getGeneration());

                String response = chat(promptProvider.staticGenerationAddData(
                        newsGenerationSurveyList, task.getGeneration(), news
                ));

                // 리스트가 1개일때 같은 세대 또는 list가 0이면 그냥 저장 공통 통계 x
                // 다른 세대이면 공통통계 ㄱ
                if(statisticsList.isEmpty() ||
                        (statisticsList.size() == 1 &&
                        statisticsList.getFirst().getGeneration() == task.getGeneration())
                ) {

                    //그냥 저장
                }else {
                   //공통 통계도 같이
                }


                // ... 처리 로직

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

}