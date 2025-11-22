package com._oormthonUNIV.newnew.news.scheduler;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.entity.NewsCategory;
import com._oormthonUNIV.newnew.news.repository.NewsCrawlerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverNewsScheduler {

    private final NaverNewsCrawler naverNewsCrawler;
    private final NewsCrawlerRepository newsCrawlerRepository;

    private static final String POLITICS_URL = "https://news.naver.com/section/100"; // 정치
    private static final String ECONOMY_URL = "https://news.naver.com/section/101"; // 경제
    private static final String SOCIETY_URL = "https://news.naver.com/section/102"; // 사회
    private static final String CULTURE_URL = "https://news.naver.com/section/103"; // 생활/문화
    private static final String IT_URL = "https://news.naver.com/section/105"; // IT/과학

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/120.0.0.0 Safari/537.36";

    @Scheduled(fixedRate = 5 * 60 * 1000) // 5분
    public void crawlAllSections() {
        crawlSection(POLITICS_URL, NewsCategory.POLITICS);
        crawlSection(ECONOMY_URL, NewsCategory.ECONOMY);
        crawlSection(SOCIETY_URL, NewsCategory.SOCIETY);
        crawlSection(CULTURE_URL, NewsCategory.CULTURE);
        crawlSection(IT_URL, NewsCategory.IT);
    }

    private void crawlSection(String sectionUrl, NewsCategory category) {
        try {
            Document doc = Jsoup.connect(sectionUrl)
                    .userAgent(USER_AGENT)
                    .timeout(10_000)
                    .get();

            Elements linkEls = doc.select("a[href*=/mnews/article/]");

            Set<String> articleUrls = new LinkedHashSet<>();
            for (Element a : linkEls) {
                String href = a.absUrl("href");
                if (href.isEmpty()) {
                    href = a.attr("href");
                }

                // 댓글 페이지는 제외
                if (href.contains("/mnews/article/")
                        && !href.contains("/article/comment/")) {
                    articleUrls.add(href);
                }
            }

            int limit = 100;
            int count = 0;

            List<News> toSave = new ArrayList<>();

            for (String articleUrl : articleUrls) {
                if (count >= limit) break;
                count++;

                log.info("기사 크롤링 시도: {}", articleUrl);

                News news = naverNewsCrawler.crawlArticle(articleUrl, category);

                if (news == null) { // 실패시
                    continue;
                }

                toSave.add(news);
            }

            if (!toSave.isEmpty()) {
                newsCrawlerRepository.saveAll(toSave);
                log.info("저장 완료: {}건", toSave.size());
            } else {
                log.info("저장할 뉴스 없음");
            }

        } catch (IOException e) {
            log.error("섹션 페이지 크롤링 실패: {}", sectionUrl, e);
        }
    }
}
