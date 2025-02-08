package com.lucasnscr.ai_financial_analyst.enums;

import lombok.Getter;

@Getter
public enum CryptoEnum {
    BITCOIN("BTC"),
    ETHEREUM("ETH"),
    XRP("XRP"),
    SOLANA("SOL");

    private final String ticker;

    CryptoEnum(String ticker) {
        this.ticker = ticker;
    }


}
