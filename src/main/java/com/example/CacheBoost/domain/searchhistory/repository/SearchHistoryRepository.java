package com.example.CacheBoost.domain.searchhistory.repository;

import com.example.CacheBoost.domain.searchhistory.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

}
