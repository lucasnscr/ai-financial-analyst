package com.lucasnscr.ai_financial_analyst.enums;

public enum CryptoEnum {
    BITCOIN("BTC"),
    ETHEREUM("ETH"),
    TETHER("USDT"),
    BINANCE_COIN("BNB"),
    RIPPLE("XRP"),
    USD_COIN("USDC"),
    DOGECOIN("DOGE"),
    SOLANA("SOL"),
    POLYGON("MATIC"),
    LITECOIN("LTC");

    private final String ticker;

    CryptoEnum(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }


}
