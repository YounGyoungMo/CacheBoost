package com.example.CacheBoost.domain.searchhistory.service;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.searchhistory.entity.SearchHistory;
import com.example.CacheBoost.domain.searchhistory.repository.SearchHistoryRepository;

import com.example.CacheBoost.domain.searchkeyword.repository.SearchKeywordRepository;
import com.example.CacheBoost.domain.user.entity.User;
import com.example.CacheBoost.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("searchHistoryCacheService")
@RequiredArgsConstructor
public class CachedSearchHistoryServiceImpl implements SearchHistoryService{

    private final SearchHistoryRepository searchHistoryRepository;
    private final SearchKeywordRepository searchKeywordRepository;
    private final UserRepository userRepository;

    @Override
    // 검색 기록 저장후 기존 캐시 삭제
    @CacheEvict(value = "searchHistories", key = "#userId")
    public void saveSearchHistory(Long userId, String bookName) {

        User user = userRepository.findByIdOrElseThrow(userId);

        if (bookName == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        SearchHistory searchHistory = SearchHistory.of(user, bookName);

        searchHistoryRepository.save(searchHistory);

        // 검색 기록이 4개가 초과되면 이전에 저장된 가장 오래된 기록부터 삭제
        // 기록이 없을 수 있으므로 orElseThrow는 사용하지 않음.
        List<SearchHistory> searchHistories = searchHistoryRepository.findAllByUserIdOrderByCreatedAt(userId);
        int maxHistoryCount = 4;
        if (searchHistories.size() > 4) {
            searchHistoryRepository.deleteAll(searchHistories.subList(0, searchHistories.size() - maxHistoryCount));
        }
    }
    
    // 검색 기록 가져온 후 캐시 저장(다음에 가져올 때는 캐시로 가져옴)
    @Override
    @Cacheable(value = "searchHistories", key = "#userId")
    public List<SearchHistoryResponseDto> getSearchHistories(Long userId) {
        List<SearchHistory> searchHistories = searchHistoryRepository.findCachedSearchHistoriesByUserId(userId);

        return searchHistories.stream()
                .map(SearchHistory::from)
                .toList();
    }

    // 데이터 모두 삭제후 캐시 모두 삭제
    @Override
    @CacheEvict(value = "searchHistories", key= "#userId")
    public void removeSearchHistory(Long userId) {
        List<SearchHistory> searchHistories = searchHistoryRepository.findAllByUserIdOrderByCreatedAt(userId);
        searchHistoryRepository.deleteAll(searchHistories);
    }

}
