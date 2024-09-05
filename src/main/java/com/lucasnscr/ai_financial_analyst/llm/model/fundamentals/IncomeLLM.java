package com.lucasnscr.ai_financial_analyst.llm.model.fundamentals;

import lombok.Data;

@Data
public class IncomeLLM {
    private String fiscalDateEnding;
    private String reportedCurrency;
    private long grossProfit;
    private long totalRevenue;
    private long costOfRevenue;
    private long costOfGoodsAndServicesSold;
    private long operatingIncome;
    private long sellingGeneralAndAdministrative;
    private long researchAndDevelopment;
    private long operatingExpenses;
    private String investmentIncomeNet;
    private long netInterestIncome;
    private long interestIncome;
    private long interestExpense;
    private long nonInterestIncome;
    private long otherNonOperatingIncome;
    private long depreciation;
    private long depreciationAndAmortization;
    private long incomeBeforeTax;
    private long incomeTaxExpense;
    private long interestAndDebtExpense;
    private long netIncomeFromContinuingOperations;
    private long comprehensiveIncomeNetOfTax;
    private long ebit;
    private long ebitda;
    private long netIncome;
}
