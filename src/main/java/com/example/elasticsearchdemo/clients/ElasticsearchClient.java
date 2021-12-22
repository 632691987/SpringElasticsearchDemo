package com.example.elasticsearchdemo.clients;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Pageable;

public interface ElasticsearchClient {

    SearchResponse search(SearchRequest searchRequest);

    SearchResult scroll(SearchRequest searchRequest, Pageable pageable, String scollId);

}
