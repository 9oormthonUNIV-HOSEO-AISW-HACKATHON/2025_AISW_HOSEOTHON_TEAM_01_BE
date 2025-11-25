package com._oormthonUNIV.newnew.news.controller;

import com._oormthonUNIV.newnew.news.dto.response.NewsDetailResponse;
import com._oormthonUNIV.newnew.news.dto.response.NewsListResponse;
import com._oormthonUNIV.newnew.news.facade.NewsFacade;
import com._oormthonUNIV.newnew.security.entity.UserDetailImpl;
import com._oormthonUNIV.newnew.user.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "news", description = "뉴스 관련 API")
@SecurityRequirement(name = "bearerAuth")
public class NewsController {

    private final NewsFacade newsFacade;

    @GetMapping("/news")
    @Operation(summary = "뉴스 목록 조회", description = "페이지네이션 또는 커서 기반(nextNewsId)으로 뉴스 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 뉴스 목록을 반환")
    })
    public ResponseEntity<NewsListResponse> getNews(
            @Parameter(description = "페이지 번호(1부터 시작)") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "커서 기반 조회를 위한 다음 뉴스 ID(해당 ID보다 작은 항목 조회)") @RequestParam(required = false) Long nextNewsId
    ) {
        NewsListResponse response = newsFacade.getNewsList(page, size, nextNewsId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/news/{newsId}") // 상세페이지
    @Operation(summary = "뉴스 상세 조회", description = "뉴스 상세 정보를 조회합니다. 인증 사용자는 설문 여부에 따라 블러 처리 여부가 반영됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 상세 정보를 반환"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 뉴스 ID")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<NewsDetailResponse> getNewsDetail(
            @Parameter(description = "뉴스 ID") @PathVariable Long newsId,
            @AuthenticationPrincipal UserDetailImpl userDetail
    ) {
        Users user = userDetail.getUser();
        NewsDetailResponse response =
                newsFacade.getNewsDetail(newsId, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/news/view-count")
    @Operation(summary = "인기 뉴스 Top2", description = "조회수 기준 상위 2개의 뉴스를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 인기 뉴스를 반환")
    public ResponseEntity<NewsListResponse> getRankingNews() {
        NewsListResponse response =
                newsFacade.getTop2News();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/news/survey")
    @Operation(summary = "사용자 설문 참여 뉴스 목록", description = "사용자가 설문에 참여한 뉴스 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 목록을 반환")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<NewsListResponse> getUserSurveyedNews(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @Parameter(description = "페이지 번호(1부터 시작)") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") Integer size
    ) {
        Users user = userDetail.getUser();
        NewsListResponse response = newsFacade.getUserSurveyedNews(user, page, size);
        return ResponseEntity.ok(response);
    }

}
