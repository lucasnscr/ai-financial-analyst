package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CryptoNewsAndSentimentalsRepository extends MongoRepository<CryptoNewsAndSentimentals, String> {
}
