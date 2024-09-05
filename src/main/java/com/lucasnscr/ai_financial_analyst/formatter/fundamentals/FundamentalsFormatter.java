package com.lucasnscr.ai_financial_analyst.formatter.fundamentals;

import com.lucasnscr.ai_financial_analyst.llm.model.fundamentals.*;
import org.springframework.stereotype.Component;

@Component
public class FundamentalsFormatter {

    public String formatFundamentalsDataCompanyLLM(FundamentalsDataCompanyLLM fundamentalsDataCompanyLLM) {
        StringBuilder sb = new StringBuilder();

        sb.append("Stock Information:\n")
                .append("Symbol: ").append(fundamentalsDataCompanyLLM.getSymbol()).append("\n")
                .append("Asset Type: ").append(fundamentalsDataCompanyLLM.getAssetType()).append("\n")
                .append("Name: ").append(fundamentalsDataCompanyLLM.getName()).append("\n")
                .append("Description: ").append(fundamentalsDataCompanyLLM.getDescription()).append("\n")
                .append("CIK: ").append(fundamentalsDataCompanyLLM.getCik()).append("\n")
                .append("Exchange: ").append(fundamentalsDataCompanyLLM.getExchange()).append("\n")
                .append("Currency: ").append(fundamentalsDataCompanyLLM.getCurrency()).append("\n")
                .append("Country: ").append(fundamentalsDataCompanyLLM.getCountry()).append("\n")
                .append("Sector: ").append(fundamentalsDataCompanyLLM.getSector()).append("\n")
                .append("Industry: ").append(fundamentalsDataCompanyLLM.getIndustry()).append("\n")
                .append("Address: ").append(fundamentalsDataCompanyLLM.getAddress()).append("\n")
                .append("Official Site: ").append(fundamentalsDataCompanyLLM.getOfficialSite()).append("\n")
                .append("Fiscal Year End: ").append(fundamentalsDataCompanyLLM.getFiscalYearEnd()).append("\n")
                .append("Latest Quarter: ").append(fundamentalsDataCompanyLLM.getLatestQuarter()).append("\n")
                .append("Market Capitalization: ").append(fundamentalsDataCompanyLLM.getMarketCapitalization()).append("\n")
                .append("EBITDA: ").append(fundamentalsDataCompanyLLM.getEbitda()).append("\n")
                .append("P/E Ratio: ").append(fundamentalsDataCompanyLLM.getPeRatio()).append("\n")
                .append("PEG Ratio: ").append(fundamentalsDataCompanyLLM.getPegRatio()).append("\n")
                .append("Book Value: ").append(fundamentalsDataCompanyLLM.getBookValue()).append("\n")
                .append("Dividend per Share: ").append(fundamentalsDataCompanyLLM.getDividendPerShare()).append("\n")
                .append("Dividend Yield: ").append(fundamentalsDataCompanyLLM.getDividendYield()).append("\n")
                .append("EPS: ").append(fundamentalsDataCompanyLLM.getEps()).append("\n")
                .append("Revenue per Share (TTM): ").append(fundamentalsDataCompanyLLM.getRevenuePerShareTTM()).append("\n")
                .append("Profit Margin: ").append(fundamentalsDataCompanyLLM.getProfitMargin()).append("\n")
                .append("Operating Margin (TTM): ").append(fundamentalsDataCompanyLLM.getOperatingMarginTTM()).append("\n")
                .append("Return on Assets (TTM): ").append(fundamentalsDataCompanyLLM.getReturnOnAssetsTTM()).append("\n")
                .append("Return on Equity (TTM): ").append(fundamentalsDataCompanyLLM.getReturnOnEquityTTM()).append("\n")
                .append("Revenue (TTM): ").append(fundamentalsDataCompanyLLM.getRevenueTTM()).append("\n")
                .append("Gross Profit (TTM): ").append(fundamentalsDataCompanyLLM.getGrossProfitTTM()).append("\n")
                .append("Diluted EPS (TTM): ").append(fundamentalsDataCompanyLLM.getDilutedEPSTTM()).append("\n")
                .append("Quarterly Earnings Growth (YOY): ").append(fundamentalsDataCompanyLLM.getQuarterlyEarningsGrowthYOY()).append("\n")
                .append("Quarterly Revenue Growth (YOY): ").append(fundamentalsDataCompanyLLM.getQuarterlyRevenueGrowthYOY()).append("\n")
                .append("Analyst Target Price: ").append(fundamentalsDataCompanyLLM.getAnalystTargetPrice()).append("\n")
                .append("Analyst Ratings:\n")
                .append("\tStrong Buy: ").append(fundamentalsDataCompanyLLM.getAnalystRatingStrongBuy()).append("\n")
                .append("\tBuy: ").append(fundamentalsDataCompanyLLM.getAnalystRatingBuy()).append("\n")
                .append("\tHold: ").append(fundamentalsDataCompanyLLM.getAnalystRatingHold()).append("\n")
                .append("\tSell: ").append(fundamentalsDataCompanyLLM.getAnalystRatingSell()).append("\n")
                .append("\tStrong Sell: ").append(fundamentalsDataCompanyLLM.getAnalystRatingStrongSell()).append("\n")
                .append("Trailing P/E: ").append(fundamentalsDataCompanyLLM.getTrailingPE()).append("\n")
                .append("Forward P/E: ").append(fundamentalsDataCompanyLLM.getForwardPE()).append("\n")
                .append("Price to Sales Ratio (TTM): ").append(fundamentalsDataCompanyLLM.getPriceToSalesRatioTTM()).append("\n")
                .append("Price to Book Ratio: ").append(fundamentalsDataCompanyLLM.getPriceToBookRatio()).append("\n")
                .append("EV to Revenue: ").append(fundamentalsDataCompanyLLM.getEvToRevenue()).append("\n")
                .append("EV to EBITDA: ").append(fundamentalsDataCompanyLLM.getEvToEBITDA()).append("\n")
                .append("Beta: ").append(fundamentalsDataCompanyLLM.getBeta()).append("\n")
                .append("52 Week High: ").append(fundamentalsDataCompanyLLM.getWeek52High()).append("\n")
                .append("52 Week Low: ").append(fundamentalsDataCompanyLLM.getWeek52Low()).append("\n")
                .append("50 Day Moving Average: ").append(fundamentalsDataCompanyLLM.getMovingAverage50Day()).append("\n")
                .append("200 Day Moving Average: ").append(fundamentalsDataCompanyLLM.getMovingAverage200Day()).append("\n")
                .append("Shares Outstanding: ").append(fundamentalsDataCompanyLLM.getSharesOutstanding()).append("\n")
                .append("Dividend Date: ").append(fundamentalsDataCompanyLLM.getDividendDate()).append("\n")
                .append("Ex-Dividend Date: ").append(fundamentalsDataCompanyLLM.getExDividendDate()).append("\n");

        sb.append("\nDividends Data:\n");
        fundamentalsDataCompanyLLM.getDividendsLLMList()
                .stream()
                .map(this::formatDividendData)
                .forEach(sb::append);

        sb.append("\nStock Split Data:\n");
        fundamentalsDataCompanyLLM.getSplitLLMList()
                .stream()
                .map(this::formatStockSplitData)
                .forEach(sb::append);

        sb.append("\nIncome Data:\n");
        fundamentalsDataCompanyLLM.getIncomeLLMList()
                .stream()
                .map(this::formatFinancialIncomeData)
                .forEach(sb::append);

        sb.append("\nBalance Data:\n");
        fundamentalsDataCompanyLLM.getBalanceLLMList()
                .stream()
                .map(this::formatBalanceLLM)
                .forEach(sb::append);

        sb.append("\nCash Flow Data:\n");
        fundamentalsDataCompanyLLM.getCashFlowLLMList()
                .stream()
                .map(this::formatCashFlowData)
                .forEach(sb::append);

        sb.append("\nEarnings Data:\n");
        fundamentalsDataCompanyLLM.getEarningsLLMList()
                .stream()
                .map(this::formatEarningsData)
                .forEach(sb::append);

        return sb.toString();
    }

    private String formatBalanceLLM(BalanceLLM balanceLLM) {
        return String.format("""
                        Financial Data for the period ending: %s
                        Reported Currency: %s
                        Total Assets: %d
                        Total Current Assets: %d
                        Cash and Cash Equivalents at Carrying Value: %d
                        Cash and Short-Term Investments: %d
                        Inventory: %d
                        Current Net Receivables: %d
                        Total Non-Current Assets: %d
                        Property, Plant, and Equipment: %d
                        Accumulated Depreciation and Amortization PPE: %s
                        Intangible Assets: %d
                        Intangible Assets Excluding Goodwill: %d
                        Goodwill: %d
                        Investments: %d
                        Long-Term Investments: %d
                        Short-Term Investments: %d
                        Other Current Assets: %d
                        Other Non-Current Assets: %s
                        Total Liabilities: %d
                        Total Current Liabilities: %d
                        Current Accounts Payable: %d
                        Deferred Revenue: %d
                        Current Debt: %d
                        Short-Term Debt: %d
                        Total Non-Current Liabilities: %d
                        Capital Lease Obligations: %d
                        Long-Term Debt: %d
                        Current Long-Term Debt: %d
                        Long-Term Debt Non-Current: %d
                        Short + Long-Term Debt Total: %d
                        Other Current Liabilities: %d
                        Other Non-Current Liabilities: %d
                        Total Shareholder Equity: %d
                        Treasury Stock: %d
                        Retained Earnings: %d
                        Common Stock: %d
                        Common Stock Shares Outstanding: %d
                        """,
                balanceLLM.getFiscalDateEnding(), balanceLLM.getReportedCurrency(), balanceLLM.getTotalAssets(),
                balanceLLM.getTotalCurrentAssets(), balanceLLM.getCashAndCashEquivalentsAtCarryingValue(),
                balanceLLM.getCashAndShortTermInvestments(), balanceLLM.getInventory(), balanceLLM.getCurrentNetReceivables(),
                balanceLLM.getTotalNonCurrentAssets(), balanceLLM.getPropertyPlantEquipment(),
                balanceLLM.getAccumulatedDepreciationAmortizationPPE(), balanceLLM.getIntangibleAssets(),
                balanceLLM.getIntangibleAssetsExcludingGoodwill(), balanceLLM.getGoodwill(), balanceLLM.getInvestments(),
                balanceLLM.getLongTermInvestments(), balanceLLM.getShortTermInvestments(), balanceLLM.getOtherCurrentAssets(),
                balanceLLM.getOtherNonCurrentAssets(), balanceLLM.getTotalLiabilities(), balanceLLM.getTotalCurrentLiabilities(),
                balanceLLM.getCurrentAccountsPayable(), balanceLLM.getDeferredRevenue(), balanceLLM.getCurrentDebt(),
                balanceLLM.getShortTermDebt(), balanceLLM.getTotalNonCurrentLiabilities(), balanceLLM.getCapitalLeaseObligations(),
                balanceLLM.getLongTermDebt(), balanceLLM.getCurrentLongTermDebt(), balanceLLM.getLongTermDebtNoncurrent(),
                balanceLLM.getShortLongTermDebtTotal(), balanceLLM.getOtherCurrentLiabilities(),
                balanceLLM.getOtherNonCurrentLiabilities(), balanceLLM.getTotalShareholderEquity(), balanceLLM.getTreasuryStock(),
                balanceLLM.getRetainedEarnings(), balanceLLM.getCommonStock(), balanceLLM.getCommonStockSharesOutstanding());
    }

    private String formatCashFlowData(CashFlowLLM cashFlowLLM) {
        return String.format("""
                        Cash Flow Data for the period ending: %s
                        Reported Currency: %s
                        Operating Cashflow: %d
                        Payments for Operating Activities: %d
                        Proceeds from Operating Activities: %s
                        Change in Operating Liabilities: %d
                        Change in Operating Assets: %d
                        Depreciation, Depletion, and Amortization: %d
                        Capital Expenditures: %d
                        Change in Receivables: %d
                        Change in Inventory: %d
                        Profit/Loss: %d
                        Cashflow from Investment: %d
                        Cashflow from Financing: %d
                        Proceeds from Repayments of Short-Term Debt: %d
                        Payments for Repurchase of Common Stock: %s
                        Payments for Repurchase of Equity: %s
                        Payments for Repurchase of Preferred Stock: %s
                        Dividend Payout: %d
                        Dividend Payout for Common Stock: %d
                        Dividend Payout for Preferred Stock: %s
                        Proceeds from Issuance of Common Stock: %s
                        Proceeds from Issuance of Long-Term Debt and Capital Securities Net: %d
                        Proceeds from Issuance of Preferred Stock: %s
                        Proceeds from Repurchase of Equity: %d
                        Proceeds from Sale of Treasury Stock: %s
                        Change in Cash and Cash Equivalents: %s
                        Change in Exchange Rate: %s
                        Net Income: %d
                        """,
                cashFlowLLM.getFiscalDateEnding(), cashFlowLLM.getReportedCurrency(), cashFlowLLM.getOperatingCashflow(),
                cashFlowLLM.getPaymentsForOperatingActivities(), cashFlowLLM.getProceedsFromOperatingActivities(),
                cashFlowLLM.getChangeInOperatingLiabilities(), cashFlowLLM.getChangeInOperatingAssets(),
                cashFlowLLM.getDepreciationDepletionAndAmortization(), cashFlowLLM.getCapitalExpenditures(),
                cashFlowLLM.getChangeInReceivables(), cashFlowLLM.getChangeInInventory(), cashFlowLLM.getProfitLoss(),
                cashFlowLLM.getCashflowFromInvestment(), cashFlowLLM.getCashflowFromFinancing(),
                cashFlowLLM.getProceedsFromRepaymentsOfShortTermDebt(), cashFlowLLM.getPaymentsForRepurchaseOfCommonStock(),
                cashFlowLLM.getPaymentsForRepurchaseOfEquity(), cashFlowLLM.getPaymentsForRepurchaseOfPreferredStock(),
                cashFlowLLM.getDividendPayout(), cashFlowLLM.getDividendPayoutCommonStock(),
                cashFlowLLM.getDividendPayoutPreferredStock(), cashFlowLLM.getProceedsFromIssuanceOfCommonStock(),
                cashFlowLLM.getProceedsFromIssuanceOfLongTermDebtAndCapitalSecuritiesNet(),
                cashFlowLLM.getProceedsFromIssuanceOfPreferredStock(), cashFlowLLM.getProceedsFromRepurchaseOfEquity(),
                cashFlowLLM.getProceedsFromSaleOfTreasuryStock(), cashFlowLLM.getChangeInCashAndCashEquivalents(),
                cashFlowLLM.getChangeInExchangeRate(), cashFlowLLM.getNetIncome());
    }

    private String formatDividendData(DividendsLLM dividendsLLM) {
        return String.format("""
                        Dividend Data:
                        Ex-Dividend Date: %s
                        Declaration Date: %s
                        Record Date: %s
                        Payment Date: %s
                        Amount: %.2f
                        """,
                dividendsLLM.getExDividendDate(), dividendsLLM.getDeclarationDate(), dividendsLLM.getRecordDate(), dividendsLLM.getPaymentDate(), dividendsLLM.getAmount());
    }

    private String formatEarningsData(EarningsLLM earningsLLM) {
        return String.format("""
                        Earnings Data:
                        Fiscal Date Ending: %s
                        Reported EPS: %.2f
                        """,
                earningsLLM.getFiscalDateEnding(), earningsLLM.getReportedEPS());
    }

    private String formatFinancialIncomeData(IncomeLLM incomeLLM) {
        return String.format("""
                        Financial Income Data:
                        Fiscal Date Ending: %s
                        Reported Currency: %s
                        Gross Profit: %d
                        Total Revenue: %d
                        Cost of Revenue: %d
                        Cost of Goods and Services Sold: %d
                        Operating Income: %d
                        Selling, General, and Administrative: %d
                        Research and Development: %d
                        Operating Expenses: %d
                        Investment Income Net: %s
                        Net Interest Income: %d
                        Interest Income: %d
                        Interest Expense: %d
                        Non-Interest Income: %d
                        Other Non-Operating Income: %d
                        Depreciation: %d
                        Depreciation and Amortization: %d
                        Income Before Tax: %d
                        Income Tax Expense: %d
                        Interest and Debt Expense: %d
                        Net Income from Continuing Operations: %d
                        Comprehensive Income Net of Tax: %d
                        EBIT: %d
                        EBITDA: %d
                        Net Income: %d
                        """,
                incomeLLM.getFiscalDateEnding(), incomeLLM.getReportedCurrency(), incomeLLM.getGrossProfit(),
                incomeLLM.getTotalRevenue(), incomeLLM.getCostOfRevenue(), incomeLLM.getCostOfGoodsAndServicesSold(),
                incomeLLM.getOperatingIncome(), incomeLLM.getSellingGeneralAndAdministrative(),
                incomeLLM.getResearchAndDevelopment(), incomeLLM.getOperatingExpenses(), incomeLLM.getInvestmentIncomeNet(),
                incomeLLM.getNetInterestIncome(), incomeLLM.getInterestIncome(), incomeLLM.getInterestExpense(),
                incomeLLM.getNonInterestIncome(), incomeLLM.getOtherNonOperatingIncome(), incomeLLM.getDepreciation(),
                incomeLLM.getDepreciationAndAmortization(), incomeLLM.getIncomeBeforeTax(), incomeLLM.getIncomeTaxExpense(),
                incomeLLM.getInterestAndDebtExpense(), incomeLLM.getNetIncomeFromContinuingOperations(),
                incomeLLM.getComprehensiveIncomeNetOfTax(), incomeLLM.getEbit(), incomeLLM.getEbitda(), incomeLLM.getNetIncome());
    }

    private String formatStockSplitData(SplitLLM splitLLM) {
        return String.format("""
                        Stock Split Data:
                        Effective Date: %s
                        Split Factor: %s
                        """,
                splitLLM.getEffectiveDate(), splitLLM.getSplitFactor());
    }
}