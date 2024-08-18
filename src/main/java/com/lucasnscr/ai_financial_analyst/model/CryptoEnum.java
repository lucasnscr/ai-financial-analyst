package com.lucasnscr.ai_financial_analyst.model;

public enum CryptoEnum {
    BITCOIN("BTC"),
    ETHEREUM("ETH"),
    TETHER("USDT"),
    BINANCE_COIN("BNB"),
    RIPPLE("XRP"),
    USD_COIN("USDC"),
    CARDANO("ADA"),
    DOGECOIN("DOGE"),
    SOLANA("SOL"),
    TRON("TRX"),
    POLYGON("MATIC"),
    LITECOIN("LTC"),
    POLKADOT("DOT"),
    SHIBA_INU("SHIB"),
    AVALANCHE("AVAX");


    private final String ticker;

    CryptoEnum(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }


}
