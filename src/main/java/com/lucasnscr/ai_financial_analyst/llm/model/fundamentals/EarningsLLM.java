package com.lucasnscr.ai_financial_analyst.llm.model.fundamentals;

import lombok.Data;

@Data
public class EarningsLLM {
    private String fiscalDateEnding;
    private double reportedEPS;
}
