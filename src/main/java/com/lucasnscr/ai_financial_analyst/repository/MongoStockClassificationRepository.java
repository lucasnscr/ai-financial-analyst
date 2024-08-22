package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.Crypto;
import com.lucasnscr.ai_financial_analyst.model.StockClassification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoStockClassificationRepository extends MongoRepository<StockClassification, String> {
}
