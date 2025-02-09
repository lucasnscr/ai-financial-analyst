package com.lucasnscr.ai_financial_analyst.llm.model.fundamentals;

import lombok.Data;

@Data
public class BalanceLLM {
    private String fiscalDateEnding;
    private String reportedCurrency;
    private long totalAssets;
    private long totalCurrentAssets;
    private long cashAndCashEquivalentsAtCarryingValue;
    private long cashAndShortTermInvestments;
    private long inventory;
    private long currentNetReceivables;
    private long totalNonCurrentAssets;
    private long propertyPlantEquipment;
    private String accumulatedDepreciationAmortizationPPE;
    private long intangibleAssets;
    private long intangibleAssetsExcludingGoodwill;
    private long goodwill;
    private long investments;
    private long longTermInvestments;
    private long shortTermInvestments;
    private long otherCurrentAssets;
    private String otherNonCurrentAssets;
    private long totalLiabilities;
    private long totalCurrentLiabilities;
    private long currentAccountsPayable;
    private long deferredRevenue;
    private long currentDebt;
    private long shortTermDebt;
    private long totalNonCurrentLiabilities;
    private long capitalLeaseObligations;
    private long longTermDebt;
    private long currentLongTermDebt;
    private long longTermDebtNoncurrent;
    private long shortLongTermDebtTotal;
    private long otherCurrentLiabilities;
    private long otherNonCurrentLiabilities;
    private long totalShareholderEquity;
    private long treasuryStock;
    private long retainedEarnings;
    private long commonStock;
    private long commonStockSharesOutstanding;
}
