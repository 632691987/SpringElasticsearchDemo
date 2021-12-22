package com.example.elasticsearchdemo.clients;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.ElasticsearchTimeoutException;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Component
public class ElasticsearchClientImpl implements ElasticsearchClient {

    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public ElasticsearchClientImpl(final RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest) {
        try {
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchTimeoutException e) {
            log.error("Count not send action to elasticsearch server", e);
            throw e;
        } catch (IOException e) {
            log.error("Count not send action to elasticsearch server", e);
            throw new ElasticsearchException(e);
        }

    }

    @Override
    public SearchResult scroll(SearchRequest searchRequest, Pageable pageable, String scollId) {
        try {
            if (StringUtils.isNotBlank(scollId)) {
                return nextScollResult(scollId);
            }

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<SearchHit> totalSearchHits = newArrayList();
            do {
                totalSearchHits.addAll(Arrays.asList(searchResponse.getHits().getHits()));
                if (totalSearchHits.size() > pageable.getPageSize()) {
                    break;
                }
                searchResponse = scoll(searchResponse.getScrollId());
            } while (!scollingIsFinish(searchResponse));

            if (scollingIsFinish(searchResponse)) {
                clearScoll(searchResponse.getScrollId());
                return searchResult(totalSearchHits, searchResponse);
            }

            return searchResult(totalSearchHits, searchResponse, searchResponse.getScrollId());
        } catch (ElasticsearchTimeoutException e) {
            log.error("Count not send action to elasticsearch server", e);
            throw e;
        } catch (IOException e) {
            log.error("Count not send action to elasticsearch server", e);
            throw new ElasticsearchException(e);
        }
    }

    private boolean scollingIsFinish(SearchResponse searchResponse) {
        return Objects.isNull(searchResponse.getHits()) || searchResponse.getHits().getHits().length == 0;
    }

    private void clearScoll(String scrollId) {
        ClearScrollRequest scrollRequest = new ClearScrollRequest();
        scrollRequest.addScrollId(scrollId);
        try {
            restHighLevelClient.clearScroll(scrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Count not send action to elasticsearch server", e);
            throw new ElasticsearchException(e);
        }
    }

    private SearchResult searchResult(List<SearchHit> totalSearchHits, SearchResponse searchResponse) {
        return new SearchResult(totalSearchHits, searchResponse.getHits().getTotalHits().value);
    }

    private SearchResult searchResult(List<SearchHit> totalSearchHits, SearchResponse searchResponse, String scollId) {
        return new SearchResult(totalSearchHits, searchResponse.getHits().getTotalHits().value, scollId);
    }

    private SearchResponse scoll(String scollId) {
        SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scollId);

        try {
            return restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchStatusException e) {
            log.error("Count not send action to elasticsearch server", e);
            throw e;
        } catch (IOException e) {
            log.error("Count not send action to elasticsearch server", e);
            throw new ElasticsearchException(e);
        }
    }

    private SearchResult nextScollResult(String scollId) {
        List<SearchHit> totalSearchHits = newArrayList();
        SearchResponse searchResponse = scoll(scollId);
        if (scollingIsFinish(searchResponse)) {
            clearScoll(searchResponse.getScrollId());
            return searchResult(totalSearchHits, searchResponse);
        }
        totalSearchHits.addAll(Arrays.asList(searchResponse.getHits().getHits()));
        return searchResult(totalSearchHits, searchResponse, searchResponse.getScrollId());
    }

}
