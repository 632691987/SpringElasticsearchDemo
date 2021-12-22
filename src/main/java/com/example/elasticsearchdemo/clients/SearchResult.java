package com.example.elasticsearchdemo.clients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.SearchHit;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private List<SearchHit> searchHits;

    private long totalHints;

    private String scrollId;

    public SearchResult(List<SearchHit> searchHits, long totalHints) {
        this.searchHits = searchHits;
        this.totalHints = totalHints;
    }
}
