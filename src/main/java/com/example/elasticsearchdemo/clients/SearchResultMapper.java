package com.example.elasticsearchdemo.clients;

import com.example.elasticsearchdemo.data.domain.Product;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SearchResultMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public SearchResultMapper(final ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.objectMapper = objectMapper;
    }

    public Page<Product> map(SearchHits searchHits, Pageable pageable) {
        List<Product> products = map(searchHits);
        return new PageImpl<>(products, pageable, searchHits.getTotalHits().value);
    }

    public List<Product> map(SearchResult searchResult) {
        return extractProducts(searchResult.getSearchHits());
    }

    public List<Product> map(SearchHits searchHits) {
        return extractProducts(Arrays.asList(searchHits.getHits()));
    }

    public List<Product> extractProducts(List<SearchHit> searchHits) {
        return searchHits.stream()
                .map(SearchHit::getSourceAsString)
                .filter(Objects::nonNull)
                .map(this::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Product map(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, Product.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }



}
