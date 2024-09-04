package com.lucasnscr.ai_financial_analyst.llm.model.classification;

import lombok.Data;


@Data
public class ClassficationLLM {

    private final String ticker;
    private final double price;
    private final double changeAmount;
    private final String changePercentage;
    private final double volume;


    public ClassficationLLM(String ticker, double price, double changeAmount, String changePercentage, double volume) {
        this.ticker = ticker;
        this.price = price;
        this.changeAmount = changeAmount;
        this.changePercentage = changePercentage;
        this.volume = volume;
    }
}
