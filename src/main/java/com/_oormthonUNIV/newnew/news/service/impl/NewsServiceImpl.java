package com._oormthonUNIV.newnew.news.service.impl;

import com._oormthonUNIV.newnew.news.entity.News;
import com._oormthonUNIV.newnew.news.exception.NewsErrorCode;
import com._oormthonUNIV.newnew.news.exception.NewsException;
import com._oormthonUNIV.newnew.news.repository.NewsRepository;
import com._oormthonUNIV.newnew.news.service.NewsService;
import com._oormthonUNIV.newnew.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public News getById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(NewsErrorCode.NEWS_NOT_FOUND));
    }

    @Override
    public List<News> getPageableNews(Integer page, Integer size, Long nextNewsId) {
        int safeSize = (size == null || size <= 0) ? 20 : size;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        if (nextNewsId != null) {
            Pageable cursorPage = PageRequest.of(0, safeSize, sort);
            return newsRepository.findAllByIdLessThanOrderByIdDesc(nextNewsId, cursorPage);
        }
        int safePage = (page == null || page <= 0) ? 1 : page;
        Pageable pageable = PageRequest.of(safePage - 1, safeSize, sort);
        return newsRepository.findAll(pageable).getContent();
    }

    @Transactional
    public News getNewsDetail(Long newsId) {
        // 존재 여부 먼저 확인 (예외 메시지는 글로벌 핸들러 포맷으로 처리)
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(NewsErrorCode.NEWS_NOT_FOUND));

        // 조회수 증가 후 엔티티 반환
        newsRepository.increaseViewCount(newsId);
        return news;
    }

    @Override
    public List<News> getRanking() {
        return newsRepository.findTop2ByOrderByViewCountDesc();
    }
}
