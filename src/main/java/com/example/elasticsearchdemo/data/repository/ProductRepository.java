package com.example.elasticsearchdemo.data.repository;

import com.example.elasticsearchdemo.data.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    Page<Product> findByNumber(String number, Pageable pageable);

}
