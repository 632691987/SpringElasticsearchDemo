package com.example.elasticsearchdemo;

import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

//    @Autowired
//    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void run(String... args) throws Exception {

    }
}
