package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.economy.EconomyData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EconomyRepository extends MongoRepository<EconomyData, String> {
}
