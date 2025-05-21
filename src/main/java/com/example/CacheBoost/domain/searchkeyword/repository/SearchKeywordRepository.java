package com.example.CacheBoost.domain.searchkeyword.repository;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.searchkeyword.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

   SearchKeyword findByKeyword(String bookName);

   List<SearchKeyword> findTop5ByOrderBySearchCntDescCreatedAtDesc();

}
