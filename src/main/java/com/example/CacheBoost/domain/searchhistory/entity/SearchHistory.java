package com.example.CacheBoost.domain.searchhistory.entity;

import com.example.CacheBoost.common.entity.BaseTimeEntity;
import com.example.CacheBoost.domain.searchhistory.controller.SearchHistoryController;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "search_history")
public class SearchHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String keyword;

    @CreatedDate
    private LocalDateTime searchedAt;

    public SearchHistory(User user, String keyword) {
        this.user = user;
        this.keyword = keyword;
    }

    public static SearchHistory of(User user, String keyword) {
        return new SearchHistory(user, keyword);
    }

    public static SearchHistoryResponseDto from(SearchHistory searchHistory) {
        return new SearchHistoryResponseDto(searchHistory.getId(), searchHistory.getUser().getId(), searchHistory.getKeyword());
    }
}
