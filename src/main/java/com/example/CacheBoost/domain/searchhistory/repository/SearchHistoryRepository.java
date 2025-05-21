package com.example.CacheBoost.domain.searchhistory.repository;

import com.example.CacheBoost.domain.searchhistory.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findAllByUserIdOrderByCreatedAt(Long userId);

    List<SearchHistory> findAllByUserId(Long userId);

}
