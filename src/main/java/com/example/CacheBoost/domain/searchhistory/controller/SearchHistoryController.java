package com.example.CacheBoost.domain.searchhistory.controller;
import com.example.CacheBoost.domain.searchhistory.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchService;



}
