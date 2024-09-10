package com.lucasnscr.ai_financial_analyst.llm.model.market;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class StockLLM {
    private String name;
    private String date;
    private String priceOpen;
    private String priceHigh;
    private String priceLow;
    private String priceClose;
    private String priceAdjustedClose;
    private String volume;
    private String dividendAmount;
    private String coefficient;
}