package com.lucasnscr.ai_financial_analyst.llm.model.fundamentals;

import lombok.Data;

@Data
public class CashFlowLLM {

    private String fiscalDateEnding;
    private String reportedCurrency;
    private long operatingCashflow;
    private long paymentsForOperatingActivities;
    private String proceedsFromOperatingActivities;
    private long changeInOperatingLiabilities;
    private long changeInOperatingAssets;
    private long depreciationDepletionAndAmortization;
    private long capitalExpenditures;
    private long changeInReceivables;
    private long changeInInventory;
    private long profitLoss;
    private long cashflowFromInvestment;
    private long cashflowFromFinancing;
    private long proceedsFromRepaymentsOfShortTermDebt;
    private String paymentsForRepurchaseOfCommonStock;
    private String paymentsForRepurchaseOfEquity;
    private String paymentsForRepurchaseOfPreferredStock;
    private long dividendPayout;
    private long dividendPayoutCommonStock;
    private String dividendPayoutPreferredStock;
    private String proceedsFromIssuanceOfCommonStock;
    private long proceedsFromIssuanceOfLongTermDebtAndCapitalSecuritiesNet;
    private String proceedsFromIssuanceOfPreferredStock;
    private long proceedsFromRepurchaseOfEquity;
    private String proceedsFromSaleOfTreasuryStock;
    private String changeInCashAndCashEquivalents;
    private String changeInExchangeRate;
    private long netIncome;

}
