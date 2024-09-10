package com.lucasnscr.ai_financial_analyst.llm.model.technical;

import lombok.Data;

@Data
public class MacdLLM {
    private String date;
    private double macd;
    private double macdSignal;
    private double macdHist;
}