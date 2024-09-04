package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.classification.StockClassification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockClassificationRepository extends MongoRepository<StockClassification, String> {
}
