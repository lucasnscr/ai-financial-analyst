package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.economy.EconomyData;
import com.lucasnscr.ai_financial_analyst.model.technical.Technical;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface TechnicalRepository extends MongoRepository<Technical, String> {
}
