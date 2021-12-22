package com.example.elasticsearchdemo.service;

import com.example.elasticsearchdemo.data.domain.Product;
import com.example.elasticsearchdemo.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public boolean createIndex() {
        return elasticsearchRestTemplate.indexOps(Product.class).create();
    }

    public void create() {
        Product product = new Product();
        productRepository.save(product);
    }

}
