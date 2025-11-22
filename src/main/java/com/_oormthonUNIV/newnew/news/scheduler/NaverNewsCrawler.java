package com._oormthonUNIV.newnew.news.scheduler;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.entity.NewsCategory;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NaverNewsCrawler {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/120.0.0.0 Safari/537.36";

    /**
     * 기사 1개 URL을 받아서 News 엔티티 1개를 만들어 돌려주는 메서드.
     * 실패하거나 댓글 페이지면 건너띔
     */
    public News crawlArticle(String articleUrl, NewsCategory category) {
        try {

            Document doc = Jsoup.connect(articleUrl)
                    .userAgent(USER_AGENT)
                    .timeout(10_000)
                    .get();

            // 1) 제목
            Element titleEl = doc.selectFirst("h2#title_area, h2.media_end_head_headline");
            String title = titleEl != null ? titleEl.text().trim() : "";

            // 2) 기자
            Element reporterEl = doc.selectFirst(".media_end_head_journalist_name, .byline_s");
            String reporter = reporterEl != null ? reporterEl.text().trim() : "";

            // 3) 작성 시간
            Element timeEl = doc.selectFirst("span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME");
            String writtenAt = "";
            if (timeEl != null) {
                writtenAt = timeEl.attr("data-date-time").trim();
                if (writtenAt.isEmpty()) {
                    writtenAt = timeEl.text().trim();
                }
            }

            // 4) 본문
            Element articleEl = doc.selectFirst("article#dic_area");
            if (articleEl == null) {
                articleEl = doc.selectFirst("#newsct_article");
            }

            String contentText = "";
            String firstImageUrl = null;

            if (articleEl != null) {
                contentText = articleEl.text().trim();

                Elements imgs = articleEl.select("img");
                for (Element img : imgs) {
                    String src = "";

                    if (img.hasAttr("data-src")) {
                        src = img.absUrl("data-src");
                        if (src.isBlank()) {
                            src = img.attr("data-src");
                        }
                    }
                    if (src.isBlank()) {
                        src = img.absUrl("src");
                    }
                    if (src.isBlank()) {
                        src = img.attr("src");
                    }

                    if (src == null || src.isBlank()) {
                        continue;
                    }

                    if (!(src.contains("imgnews.pstatic.net") || src.contains("mimgnews.pstatic.net"))) {
                        // continue;
                    }

                    firstImageUrl = src;
                    break;
                }
            }

            if (firstImageUrl == null || firstImageUrl.isBlank()) {
                Element ogImg = doc.selectFirst("meta[property=og:image]");
                if (ogImg != null) {
                    String ogUrl = ogImg.attr("content");
                    if (ogUrl != null && !ogUrl.isBlank()) {
                        firstImageUrl = ogUrl;
                    }
                }
            }

            System.out.println("====== 기사 크롤링 결과 ======");
            System.out.println("URL       : " + articleUrl);
            System.out.println("제목      : " + title);
            System.out.println("기자      : " + reporter);
            System.out.println("작성시간  : " + writtenAt);
            System.out.println("본문길이  : " + contentText.length());
            System.out.println("이미지URL : " + firstImageUrl);
            System.out.println("================================");

            return News.builder()
                    .title(title)
                    .author(reporter)
                    .content(contentText)
                    .category(category)              // 섹션에 따라 다르게
                    .news_created_at(LocalDateTime.now())
                    .build();

        } catch (IOException e) {
            System.err.println("기사 크롤링 실패: " + articleUrl);
            e.printStackTrace();
            return null;
        }
    }
}
