package com.example.CacheBoost.domain.searchhistory.repository;

import com.example.CacheBoost.domain.searchhistory.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findAllByUserId(Long userId);

    List<SearchHistory> findAllByUserIdOrderByCreatedAt(Long userId);

    List<SearchHistory> findCachedSearchHistoriesByUserId(Long userId);

    Page<SearchHistory> findByUserIdAndKeywordContaining(Long userId, String keywords, Pageable pageable);
}
