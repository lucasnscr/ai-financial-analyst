package com.lucasnscr.ai_financial_analyst.enums;

public enum CryptoEnum {
    BITCOIN("BTC"),
    ETHEREUM("ETH"),
    RIPPLE("XRP"),
    SOLANA("SOL");

    private final String ticker;

    CryptoEnum(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }


}
