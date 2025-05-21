package com.example.CacheBoost.domain.searchhistory.service;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.searchhistory.entity.SearchHistory;
import com.example.CacheBoost.domain.searchhistory.repository.SearchHistoryRepository;
import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.SearchKeywordResponseDto;
import com.example.CacheBoost.domain.user.entity.Role;
import com.example.CacheBoost.domain.user.entity.User;
import com.example.CacheBoost.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.CacheBoost.common.exception.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<SearchHistoryResponseDto> saveSearchHistory(User user, String bookName) {

        if (user.getId() == null || bookName == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        userRepository.save(user);

        SearchHistory searchHistory = SearchHistory.of(user, bookName);

        searchHistoryRepository.save(searchHistory);

        // 검색 기록이 4개가 초과되면 이전에 저장된 가장 오래된 기록부터 삭제
        // 기록이 없을 수 있으므로 orElseThrow는 사용하지 않음.
        List<SearchHistory> searchHistories = searchHistoryRepository.findAllByUserIdOrderByCreatedAt(user.getId());
        int maxHistoryCount = 4;
        if (searchHistories.size() > 4) {
            searchHistoryRepository.deleteAll(searchHistories.subList(0, searchHistories.size() - maxHistoryCount));
        }

        return searchHistories.stream()
                .map(SearchHistory::from)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchHistoryResponseDto> getSearchHistories(Long userId) {
        List<SearchHistory> searchHistories = searchHistoryRepository.findAllByUserId(userId);

        return searchHistories.stream()
                .map(SearchHistory::from)
                .toList();
    }

    @Override
    public void removeSearchHistory(Long userId) {
        List<SearchHistory> searchHistories = searchHistoryRepository.findAllByUserIdOrderByCreatedAt(userId);
        searchHistoryRepository.delete(searchHistories.get(0));
    }

    @Override
    public List<SearchKeywordResponseDto> getSearchKeywords() {

        return List.of();
    }
}
