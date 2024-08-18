package com.lucasnscr.ai_financial_analyst.model;

public enum StockEnum {
    APPLE("AAPL"),
    MICROSOFT("MSFT"),
    AMAZON("AMZN"),
    ALPHABET_CLASS_A("GOOGL"),
    ALPHABET_CLASS_C("GOOG"),
    FACEBOOK("META"),
    TESLA("TSLA"),
    BERKSHIRE_HATHAWAY("BRK.B"),
    NVIDIA("NVDA"),
    J_P_MORGAN_CHASE("JPM");

    private final String ticker;

    StockEnum(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }


}
