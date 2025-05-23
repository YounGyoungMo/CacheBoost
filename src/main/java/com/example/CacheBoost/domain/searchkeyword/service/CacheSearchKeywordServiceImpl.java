package com.example.CacheBoost.domain.searchkeyword.service;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.SearchKeywordResponseDto;
import com.example.CacheBoost.domain.searchkeyword.entity.SearchKeyword;
import com.example.CacheBoost.domain.searchkeyword.repository.SearchKeywordRepository;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("searchKeywordCacheService")
public class CacheSearchKeywordServiceImpl implements SearchKeywordService {

    private final SearchKeywordRepository searchKeywordRepository;

    private final StringRedisTemplate redisTemplate;
    private static final String REDIS_KEY = "popular:keywords";

    public CacheSearchKeywordServiceImpl(SearchKeywordRepository searchKeywordRepository,
        StringRedisTemplate redisTemplate) {
        this.searchKeywordRepository = searchKeywordRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveSearchKeyword(String bookName) {

        String cached = redisTemplate.opsForValue().get(REDIS_KEY);
        if (cached != null) {
            SearchKeyword searchKeyword = searchKeywordRepository.findByKeyword(bookName);

            // 검색이 되면 책 이름 별로 점수를 부여하여 Redis에 저장하여 인기 도서 점수 집계
            redisTemplate.opsForZSet().incrementScore(REDIS_KEY, bookName, 1);

            searchKeywordRepository.save(searchKeyword);
        }

    }

    @Override
    public List<SearchKeywordResponseDto> getSearchKeywords() {
        Set<String> top5Keyword = redisTemplate.opsForZSet().reverseRange(REDIS_KEY, 0, 4);

        if (top5Keyword == null || top5Keyword.isEmpty()) {
            // 캐시에 없으면 DB에서 조회
            List<SearchKeyword> topKeywords = searchKeywordRepository.findTop5ByOrderBySearchCntDescCreatedAtDesc();

            if (topKeywords.isEmpty()) {
                throw new CustomException(ErrorCode.SEARCH_KEYWORD_NOT_FOUND);
            }

            // Redis에 저장
            topKeywords.forEach(keyword ->
                redisTemplate.opsForZSet()
                    .add(REDIS_KEY, keyword.getKeyword(), keyword.getSearchCnt())
            );

            return topKeywords.stream()
                .map(SearchKeywordResponseDto::from)
                .toList();
        }

        // Redis에서 가져온 keyword 문자열들을 DTO로 변환
        return top5Keyword.stream()
            .map(SearchKeywordResponseDto::fromKeyword)
            .toList();
    }

}
