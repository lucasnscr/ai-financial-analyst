package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.StockNewsAndSentimentals;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockNewsAndSentimentalsRepository extends MongoRepository<StockNewsAndSentimentals, String> {
}
