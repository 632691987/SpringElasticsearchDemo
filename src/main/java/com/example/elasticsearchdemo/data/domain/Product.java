package com.example.elasticsearchdemo.data.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Document(indexName = "#{@environment.getProperty('spring.elasticsearch.index')}", createIndex = false)
@Data
@NoArgsConstructor
public class Product implements Persistable<String>, Serializable {

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Keyword)
    private String number;

    @Override
    public boolean isNew() {
        return false;
    }
}
