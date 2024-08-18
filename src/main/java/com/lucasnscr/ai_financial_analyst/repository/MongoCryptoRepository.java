package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.Crypto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCryptoRepository extends MongoRepository<Crypto, String> {
}
