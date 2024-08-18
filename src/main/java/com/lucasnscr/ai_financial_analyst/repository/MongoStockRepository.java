package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoStockRepository extends MongoRepository<Stock, String> {
}
