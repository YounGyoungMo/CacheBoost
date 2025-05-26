package com.example.CacheBoost.domain.searchkeyword.service;

import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.PopularKeywordTop5ResponseDto;
import com.example.CacheBoost.domain.searchkeyword.entity.SearchKeyword;
import com.example.CacheBoost.domain.searchkeyword.repository.SearchKeywordRepository;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CacheSearchKeywordService {

    private final SearchKeywordRepository searchKeywordRepository;

    private final StringRedisTemplate redisTemplate;
    private static final String REDIS_KEY = "popular:keywords";

    public void saveSearchKeyword(String bookName) {

        SearchKeyword searchKeyword = searchKeywordRepository.findByKeyword(bookName);
        if (searchKeyword != null) {
            searchKeywordRepository.save(searchKeyword);

        }

        // 검색이 되면 책 이름 별로 점수를 부여하여 Redis에 저장하여 인기 도서 점수 집계
        redisTemplate.opsForZSet().incrementScore(REDIS_KEY, bookName, 1);

        // TTL 설정 (없을 경우에만 설정)
        Boolean hasExpire = redisTemplate.getExpire(REDIS_KEY) > 0;
        if (!hasExpire) {
            redisTemplate.expire(REDIS_KEY, Duration.ofMinutes(10));
        }

    }

    public List<PopularKeywordTop5ResponseDto> getSearchKeywords() {
        Set<ZSetOperations.TypedTuple<String>> top5Keyword = redisTemplate.opsForZSet().reverseRangeWithScores(REDIS_KEY, 0, 4);

        if (top5Keyword == null) {
            return Collections.emptyList();
        }

        // Redis에서 가져온 keyword 문자열들을 DTO로 변환
        return top5Keyword.stream()
            .map(tuple -> new PopularKeywordTop5ResponseDto(tuple.getValue(),tuple.getScore()))
            .toList();
    }

}
