package com.example.CacheBoost.domain.searchkeyword.service;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.SearchKeywordResponseDto;
import com.example.CacheBoost.domain.searchkeyword.entity.SearchKeyword;
import com.example.CacheBoost.domain.searchkeyword.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchKeywordServiceImpl implements SearchKeywordService {

    private final SearchKeywordRepository searchKeywordRepository;

    @Override
    public void saveSearchKeyword(String bookName) {
        SearchKeyword searchKeyword = searchKeywordRepository.findByKeyword(bookName);
        if (searchKeyword == null) {
            searchKeyword = SearchKeyword.of(bookName, 1L);
        } else {
            searchKeyword.incrementSearchCnt();
        }

        searchKeywordRepository.save(searchKeyword);
    }

    @Override
    public List<SearchKeywordResponseDto> getSearchKeywords() {
        List<SearchKeyword> topKeywords =  searchKeywordRepository.findTop5ByOrderBySearchCntDescCreatedAtDesc();

        if (topKeywords.isEmpty()) {
            throw new CustomException(ErrorCode.SEARCH_KEYWORD_NOT_FOUND);
        }

        return topKeywords.stream()
                .map(SearchKeywordResponseDto::from)
                .toList();
    }

}
