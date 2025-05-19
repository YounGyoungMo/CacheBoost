package com.example.CacheBoost.domain.searchhistory.entity;

import com.example.CacheBoost.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "search_history")
public class SearchHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    @CreatedDate
    private LocalDateTime searchedAt;



}
