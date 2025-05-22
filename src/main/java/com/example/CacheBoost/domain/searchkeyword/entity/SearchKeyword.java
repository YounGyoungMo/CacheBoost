package com.example.CacheBoost.domain.searchkeyword.entity;

import com.example.CacheBoost.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "search_keyword")
@NoArgsConstructor
public class SearchKeyword extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    @Column(nullable = false)
    private Long searchCnt;

    public SearchKeyword(String keyword, Long searchCnt) {
        this.keyword = keyword;
        this.searchCnt = searchCnt;
    }

    public void incrementSearchCnt() {
        searchCnt++;
    }

    public static SearchKeyword of(String keyword, Long searchCnt) {
        return new SearchKeyword(keyword, searchCnt);
    }
}
