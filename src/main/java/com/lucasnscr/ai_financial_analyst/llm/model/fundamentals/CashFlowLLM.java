package com.lucasnscr.ai_financial_analyst.llm.model.fundamentals;

import lombok.Data;

@Data
public class CashFlowLLM {

    private String fiscalDateEnding;
    private String reportedCurrency;
    private long operatingCashflow;
    private long paymentsForOperatingActivities;
    private String proceedsFromOperatingActivities; // can be "None"
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
    private String paymentsForRepurchaseOfCommonStock; // can be "None"
    private String paymentsForRepurchaseOfEquity; // can be "None"
    private String paymentsForRepurchaseOfPreferredStock; // can be "None"
    private long dividendPayout;
    private long dividendPayoutCommonStock;
    private String dividendPayoutPreferredStock; // can be "None"
    private String proceedsFromIssuanceOfCommonStock; // can be "None"
    private long proceedsFromIssuanceOfLongTermDebtAndCapitalSecuritiesNet;
    private String proceedsFromIssuanceOfPreferredStock; // can be "None"
    private long proceedsFromRepurchaseOfEquity;
    private String proceedsFromSaleOfTreasuryStock; // can be "None"
    private String changeInCashAndCashEquivalents; // can be "None"
    private String changeInExchangeRate; // can be "None"
    private long netIncome;

}
