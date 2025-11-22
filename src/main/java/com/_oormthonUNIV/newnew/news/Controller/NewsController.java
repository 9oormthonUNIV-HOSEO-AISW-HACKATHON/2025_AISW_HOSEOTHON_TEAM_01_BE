package com._oormthonUNIV.newnew.news.Controller;

import com._oormthonUNIV.newnew.news.DTO.response.NewsDetailResponseDto;
import com._oormthonUNIV.newnew.news.DTO.response.NewsListResponseDto;
import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.service.impl.NewsServiceImpl;
import com._oormthonUNIV.newnew.security.entity.UserDetailImpl;
import com._oormthonUNIV.newnew.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewsController {

    private final NewsServiceImpl newsServiceImpl;

    @GetMapping("/News")
    public NewsListResponseDto getNews() {
        return newsServiceImpl.getNewsList();
    }


    @GetMapping("/News/{newsId}") // 상세페이지
    public NewsDetailResponseDto getNewsDetail(
            @PathVariable Long newsId,
            @AuthenticationPrincipal UserDetailImpl userDetail
    ) {
        Users user = userDetail.getUser();
        News news = newsServiceImpl.getNewsDetail(newsId);

        // 예시) 이 유저가 이미 이 뉴스에 대해 신고/리포트를 봐야 블러 처리하는 로직이 있다면 여기서 계산
        boolean reportBlur = false; // 지금은 하드코딩, 나중에 로직 붙이면 됨

        return NewsDetailResponseDto.of(news, reportBlur);
    }
}
