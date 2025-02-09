package com.lucasnscr.ai_financial_analyst.llm.model.classification;

import lombok.Data;


@Data
public class ClassficationLLM {
    private final String ticker;
    private final double price;
    private final double changeAmount;
    private final String changePercentage;
    private final double volume;
}
