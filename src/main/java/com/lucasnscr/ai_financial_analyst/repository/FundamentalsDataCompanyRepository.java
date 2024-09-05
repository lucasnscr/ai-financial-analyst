package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.fundamentals.FundamentalsDataCompany;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundamentalsDataCompanyRepository extends MongoRepository<FundamentalsDataCompany, String> {
}
