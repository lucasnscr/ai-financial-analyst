package com.lucasnscr.ai_financial_analyst.enums;

import lombok.Getter;

@Getter
public enum StockEnum {
    APPLE("AAPL"),
    FACEBOOK("META"),
    TESLA("TSLA"),
    NVIDIA("NVDA");

    private final String ticker;

    StockEnum(String ticker) {
        this.ticker = ticker;
    }


}
