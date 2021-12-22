package com.example.elasticsearchdemo.config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfiguration {

    @Bean
    public RestClient restClient(RestHighLevelClient restHighLevelClient) {
        return restHighLevelClient.getLowLevelClient();
    }
}
