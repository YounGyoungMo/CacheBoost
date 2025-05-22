package com.example.CacheBoost.domain.searchkeyword.repository;

import com.example.CacheBoost.domain.searchkeyword.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

   SearchKeyword findByKeyword(String bookName);

   List<SearchKeyword> findTop5ByOrderBySearchCntDescCreatedAtDesc();

}
