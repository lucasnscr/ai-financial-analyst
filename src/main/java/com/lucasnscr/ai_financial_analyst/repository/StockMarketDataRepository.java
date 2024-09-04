package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.market.Stock;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockMarketDataRepository extends MongoRepository<Stock, String> {
}
