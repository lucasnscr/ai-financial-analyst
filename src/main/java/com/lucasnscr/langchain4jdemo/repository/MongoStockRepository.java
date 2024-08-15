package com.lucasnscr.langchain4jdemo.repository;

import com.lucasnscr.langchain4jdemo.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoStockRepository extends MongoRepository<Stock, String> {
}
