package com.lucasnscr.ai_financial_analyst.converter.fundamentals;

import com.lucasnscr.ai_financial_analyst.formatter.fundamentals.FundamentalsFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.fundamentals.*;
import com.lucasnscr.ai_financial_analyst.model.fundamentals.FundamentalsDataCompany;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;

@Component
public class FundamentalsConverter {

    private final FundamentalsFormatter fundamentalsFormatter;

    private static final String OVERVIEW = "OVERVIEW";
    private static final String DIVIDENDS = "DIVIDENDS";
    private static final String SPLITS = "SPLITS";
    private static final String INCOME_STATEMENT = "INCOME_STATEMENT";
    private static final String BALANCE_SHEET = "BALANCE_SHEET";
    private static final String CASH_FLOW = "CASH_FLOW";
    private static final String EARNINGS = "EARNINGS";

    @Autowired
    public FundamentalsConverter(FundamentalsFormatter fundamentalsFormatter) {
        this.fundamentalsFormatter = fundamentalsFormatter;
    }

    public FundamentalsDataCompany buildFundamentalsDataCompany(String name, Map<String, JSONObject> responseData) {
        FundamentalsDataCompany dataCompany = null;
        String content = "";

        if (!ObjectUtils.isEmpty(responseData)) {
            dataCompany = new FundamentalsDataCompany();
            FundamentalsDataCompanyLLM fundamentalsDataCompanyLLM = new FundamentalsDataCompanyLLM();
            List<DividendsLLM> dividendsLLMList;
            List<SplitLLM> splitLLMList;
            List<IncomeLLM> incomeLLMList;
            List<BalanceLLM> balanceLLMList;
            List<CashFlowLLM> cashFlowLLMList;
            List<EarningsLLM> earningsLLMList;

            for (Map.Entry<String, JSONObject> entry : responseData.entrySet()) {
                String key = entry.getKey();
                JSONObject jsonData = entry.getValue();

                switch (key) {
                    case OVERVIEW:
                        fundamentalsDataCompanyLLM = buildFundamentalsDataOverview(jsonData);
                        break;
                    case DIVIDENDS:
                        dividendsLLMList = buildFundamentalsDataDividends(jsonData);
                        fundamentalsDataCompanyLLM.setDividendsLLMList(dividendsLLMList);
                        break;
                    case SPLITS:
                        splitLLMList = buildFundamentalsDataSplits(jsonData);
                        fundamentalsDataCompanyLLM.setSplitLLMList(splitLLMList);
                        break;
                    case INCOME_STATEMENT:
                        incomeLLMList = buildFundamentalsDataIncome(jsonData);
                        fundamentalsDataCompanyLLM.setIncomeLLMList(incomeLLMList);
                        break;
                    case BALANCE_SHEET:
                        balanceLLMList = buildFundamentalsDataBalance(jsonData);
                        fundamentalsDataCompanyLLM.setBalanceLLMList(balanceLLMList);
                        break;
                    case CASH_FLOW:
                        cashFlowLLMList = buildFundamentalsDataCashFlow(jsonData);
                        fundamentalsDataCompanyLLM.setCashFlowLLMList(cashFlowLLMList);
                        break;
                    case EARNINGS:
                        earningsLLMList = buildFundamentalsEarnings(jsonData);
                        fundamentalsDataCompanyLLM.setEarningsLLMList(earningsLLMList);
                        break;
                    default:
                        break;
                }
            }
            if (!ObjectUtils.isEmpty(fundamentalsDataCompanyLLM)) {
                content = fundamentalsFormatter.formatFundamentalsDataCompanyLLM(fundamentalsDataCompanyLLM);
            }
            dataCompany.setName(name);
            dataCompany.setDate(LocalDate.now().toString());
            dataCompany.setContentForLLM(Collections.singletonList(content));
        }
        return dataCompany;
    }

    private FundamentalsDataCompanyLLM buildFundamentalsDataOverview(JSONObject jsonObject) {
        FundamentalsDataCompanyLLM fundamentalsDataCompanyLLM = new FundamentalsDataCompanyLLM();
        fundamentalsDataCompanyLLM.setSymbol(jsonObject.optString("Symbol"));
        fundamentalsDataCompanyLLM.setAssetType(jsonObject.optString("AssetType"));
        fundamentalsDataCompanyLLM.setName(jsonObject.optString("Name"));
        fundamentalsDataCompanyLLM.setDescription(jsonObject.optString("Description"));
        fundamentalsDataCompanyLLM.setCik(jsonObject.optString("CIK"));
        fundamentalsDataCompanyLLM.setExchange(jsonObject.optString("Exchange"));
        fundamentalsDataCompanyLLM.setCurrency(jsonObject.optString("Currency"));
        fundamentalsDataCompanyLLM.setCountry(jsonObject.optString("Country"));
        fundamentalsDataCompanyLLM.setSector(jsonObject.optString("Sector"));
        fundamentalsDataCompanyLLM.setIndustry(jsonObject.optString("Industry"));
        fundamentalsDataCompanyLLM.setAddress(jsonObject.optString("Address"));
        fundamentalsDataCompanyLLM.setOfficialSite(jsonObject.optString("OfficialSite"));
        fundamentalsDataCompanyLLM.setFiscalYearEnd(jsonObject.optString("FiscalYearEnd"));
        fundamentalsDataCompanyLLM.setLatestQuarter(jsonObject.optString("LatestQuarter"));
        fundamentalsDataCompanyLLM.setMarketCapitalization(jsonObject.optLong("MarketCapitalization"));
        fundamentalsDataCompanyLLM.setEbitda(jsonObject.optLong("EBITDA"));
        fundamentalsDataCompanyLLM.setPeRatio(jsonObject.optDouble("PERatio"));
        fundamentalsDataCompanyLLM.setPegRatio(jsonObject.optDouble("PEGRatio"));
        fundamentalsDataCompanyLLM.setBookValue(jsonObject.optDouble("BookValue"));
        fundamentalsDataCompanyLLM.setDividendPerShare(jsonObject.optDouble("DividendPerShare"));
        fundamentalsDataCompanyLLM.setDividendYield(jsonObject.optDouble("DividendYield"));
        fundamentalsDataCompanyLLM.setEps(jsonObject.optDouble("EPS"));
        fundamentalsDataCompanyLLM.setRevenuePerShareTTM(jsonObject.optDouble("RevenuePerShareTTM"));
        fundamentalsDataCompanyLLM.setProfitMargin(jsonObject.optDouble("ProfitMargin"));
        fundamentalsDataCompanyLLM.setOperatingMarginTTM(jsonObject.optDouble("OperatingMarginTTM"));
        fundamentalsDataCompanyLLM.setReturnOnAssetsTTM(jsonObject.optDouble("ReturnOnAssetsTTM"));
        fundamentalsDataCompanyLLM.setReturnOnEquityTTM(jsonObject.optDouble("ReturnOnEquityTTM"));
        fundamentalsDataCompanyLLM.setRevenueTTM(jsonObject.optLong("RevenueTTM"));
        fundamentalsDataCompanyLLM.setGrossProfitTTM(jsonObject.optLong("GrossProfitTTM"));
        fundamentalsDataCompanyLLM.setDilutedEPSTTM(jsonObject.optDouble("DilutedEPSTTM"));
        fundamentalsDataCompanyLLM.setQuarterlyEarningsGrowthYOY(jsonObject.optDouble("QuarterlyEarningsGrowthYOY"));
        fundamentalsDataCompanyLLM.setQuarterlyRevenueGrowthYOY(jsonObject.optDouble("QuarterlyRevenueGrowthYOY"));
        fundamentalsDataCompanyLLM.setAnalystTargetPrice(jsonObject.optDouble("AnalystTargetPrice"));
        fundamentalsDataCompanyLLM.setAnalystRatingStrongBuy(jsonObject.optInt("AnalystRatingStrongBuy"));
        fundamentalsDataCompanyLLM.setAnalystRatingBuy(jsonObject.optInt("AnalystRatingBuy"));
        fundamentalsDataCompanyLLM.setAnalystRatingHold(jsonObject.optInt("AnalystRatingHold"));
        fundamentalsDataCompanyLLM.setAnalystRatingSell(jsonObject.optInt("AnalystRatingSell"));
        fundamentalsDataCompanyLLM.setAnalystRatingStrongSell(jsonObject.optInt("AnalystRatingStrongSell"));
        fundamentalsDataCompanyLLM.setTrailingPE(jsonObject.optDouble("TrailingPE"));
        fundamentalsDataCompanyLLM.setForwardPE(jsonObject.optDouble("ForwardPE"));
        fundamentalsDataCompanyLLM.setPriceToSalesRatioTTM(jsonObject.optDouble("PriceToSalesRatioTTM"));
        fundamentalsDataCompanyLLM.setPriceToBookRatio(jsonObject.optDouble("PriceToBookRatio"));
        fundamentalsDataCompanyLLM.setEvToRevenue(jsonObject.optDouble("EVToRevenue"));
        fundamentalsDataCompanyLLM.setEvToEBITDA(jsonObject.optDouble("EVToEBITDA"));
        fundamentalsDataCompanyLLM.setBeta(jsonObject.optDouble("Beta"));
        fundamentalsDataCompanyLLM.setWeek52High(jsonObject.optDouble("52WeekHigh"));
        fundamentalsDataCompanyLLM.setWeek52Low(jsonObject.optDouble("52WeekLow"));
        fundamentalsDataCompanyLLM.setMovingAverage50Day(jsonObject.optDouble("50DayMovingAverage"));
        fundamentalsDataCompanyLLM.setMovingAverage200Day(jsonObject.optDouble("200DayMovingAverage"));
        fundamentalsDataCompanyLLM.setSharesOutstanding(jsonObject.optLong("SharesOutstanding"));
        fundamentalsDataCompanyLLM.setDividendDate(jsonObject.optString("DividendDate"));
        fundamentalsDataCompanyLLM.setExDividendDate(jsonObject.optString("ExDividendDate"));
        return fundamentalsDataCompanyLLM;
    }

    private List<DividendsLLM> buildFundamentalsDataDividends(JSONObject value) {
        List<DividendsLLM> dividendsLLMS = new ArrayList<>();
        JSONArray dataArray = value.optJSONArray("data");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dividendJson = dataArray.optJSONObject(i);
                if (dividendJson != null) {
                    DividendsLLM dividendsLLM = new DividendsLLM();
                    dividendsLLM.setExDividendDate(dividendJson.optString("ex_dividend_date"));
                    dividendsLLM.setDeclarationDate(dividendJson.optString("declaration_date"));
                    dividendsLLM.setRecordDate(dividendJson.optString("record_date"));
                    dividendsLLM.setPaymentDate(dividendJson.optString("payment_date"));
                    dividendsLLM.setAmount(dividendJson.optDouble("amount"));
                    dividendsLLMS.add(dividendsLLM);
                }
            }
        }
        return dividendsLLMS;
    }

    private List<SplitLLM> buildFundamentalsDataSplits(JSONObject value) {
        List<SplitLLM> splitLLMS = new ArrayList<>();
        JSONArray dataArray = value.optJSONArray("data");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject splitJson = dataArray.optJSONObject(i);
                if (splitJson != null) {
                    SplitLLM splitLLM = new SplitLLM();
                    splitLLM.setEffectiveDate(splitJson.optString("effective_date"));
                    splitLLM.setSplitFactor(splitJson.optString("split_factor"));
                    splitLLMS.add(splitLLM);
                }
            }
        }
        return splitLLMS;
    }

    private List<IncomeLLM> buildFundamentalsDataIncome(JSONObject value) {
        List<IncomeLLM> incomeLLMList = new ArrayList<>();
        JSONArray dataArray = value.optJSONArray("annualReports");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject incomeJson = dataArray.optJSONObject(i);
                if (incomeJson != null) {
                    IncomeLLM incomeLLM = new IncomeLLM();
                    incomeLLM.setFiscalDateEnding(incomeJson.optString("fiscalDateEnding"));
                    incomeLLM.setReportedCurrency(incomeJson.optString("reportedCurrency"));
                    incomeLLM.setGrossProfit(incomeJson.optLong("grossProfit"));
                    incomeLLM.setTotalRevenue(incomeJson.optLong("totalRevenue"));
                    incomeLLM.setCostOfRevenue(incomeJson.optLong("costOfRevenue"));
                    incomeLLM.setCostOfGoodsAndServicesSold(incomeJson.optLong("costofGoodsAndServicesSold"));
                    incomeLLM.setOperatingIncome(incomeJson.optLong("operatingIncome"));
                    incomeLLM.setSellingGeneralAndAdministrative(incomeJson.optLong("sellingGeneralAndAdministrative"));
                    incomeLLM.setResearchAndDevelopment(incomeJson.optLong("researchAndDevelopment"));
                    incomeLLM.setOperatingExpenses(incomeJson.optLong("operatingExpenses"));
                    incomeLLM.setInvestmentIncomeNet(incomeJson.optString("investmentIncomeNet"));
                    incomeLLM.setNetInterestIncome(incomeJson.optLong("netInterestIncome"));
                    incomeLLM.setInterestIncome(incomeJson.optLong("interestIncome"));
                    incomeLLM.setInterestExpense(incomeJson.optLong("interestExpense"));
                    incomeLLM.setNonInterestIncome(incomeJson.optLong("nonInterestIncome"));
                    incomeLLM.setOtherNonOperatingIncome(incomeJson.optLong("otherNonOperatingIncome"));
                    incomeLLM.setDepreciation(incomeJson.optLong("depreciation"));
                    incomeLLM.setDepreciationAndAmortization(incomeJson.optLong("depreciationAndAmortization"));
                    incomeLLM.setIncomeBeforeTax(incomeJson.optLong("incomeBeforeTax"));
                    incomeLLM.setIncomeTaxExpense(incomeJson.optLong("incomeTaxExpense"));
                    incomeLLM.setInterestAndDebtExpense(incomeJson.optLong("interestAndDebtExpense"));
                    incomeLLM.setNetIncomeFromContinuingOperations(incomeJson.optLong("netIncomeFromContinuingOperations"));
                    incomeLLM.setComprehensiveIncomeNetOfTax(incomeJson.optLong("comprehensiveIncomeNetOfTax"));
                    incomeLLM.setEbit(incomeJson.optLong("ebit"));
                    incomeLLM.setEbitda(incomeJson.optLong("ebitda"));
                    incomeLLM.setNetIncome(incomeJson.optLong("netIncome"));
                    incomeLLMList.add(incomeLLM);
                }
            }
        }
        return incomeLLMList;
    }

    private List<BalanceLLM> buildFundamentalsDataBalance(JSONObject value) {
        List<BalanceLLM> balanceLLMList = new ArrayList<>();
        JSONArray dataArray = value.optJSONArray("annualReports");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject balanceJson = dataArray.optJSONObject(i);
                if (balanceJson != null) {
                    BalanceLLM balanceLLM = new BalanceLLM();
                    balanceLLM.setFiscalDateEnding(balanceJson.optString("fiscalDateEnding"));
                    balanceLLM.setReportedCurrency(balanceJson.optString("reportedCurrency"));
                    balanceLLM.setTotalAssets(balanceJson.optLong("totalAssets"));
                    balanceLLM.setTotalCurrentAssets(balanceJson.optLong("totalCurrentAssets"));
                    balanceLLM.setCashAndCashEquivalentsAtCarryingValue(balanceJson.optLong("cashAndCashEquivalentsAtCarryingValue"));
                    balanceLLM.setCashAndShortTermInvestments(balanceJson.optLong("cashAndShortTermInvestments"));
                    balanceLLM.setInventory(balanceJson.optLong("inventory"));
                    balanceLLM.setCurrentNetReceivables(balanceJson.optLong("currentNetReceivables"));
                    balanceLLM.setTotalNonCurrentAssets(balanceJson.optLong("totalNonCurrentAssets"));
                    balanceLLM.setPropertyPlantEquipment(balanceJson.optLong("propertyPlantEquipment"));
                    balanceLLM.setAccumulatedDepreciationAmortizationPPE(balanceJson.optString("accumulatedDepreciationAmortizationPPE"));
                    balanceLLM.setIntangibleAssets(balanceJson.optLong("intangibleAssets"));
                    balanceLLM.setIntangibleAssetsExcludingGoodwill(balanceJson.optLong("intangibleAssetsExcludingGoodwill"));
                    balanceLLM.setGoodwill(balanceJson.optLong("goodwill"));
                    balanceLLM.setInvestments(balanceJson.optLong("investments"));
                    balanceLLM.setLongTermInvestments(balanceJson.optLong("longTermInvestments"));
                    balanceLLM.setShortTermInvestments(balanceJson.optLong("shortTermInvestments"));
                    balanceLLM.setOtherCurrentAssets(balanceJson.optLong("otherCurrentAssets"));
                    balanceLLM.setOtherNonCurrentAssets(balanceJson.optString("otherNonCurrentAssets"));
                    balanceLLM.setTotalLiabilities(balanceJson.optLong("totalLiabilities"));
                    balanceLLM.setTotalCurrentLiabilities(balanceJson.optLong("totalCurrentLiabilities"));
                    balanceLLM.setCurrentAccountsPayable(balanceJson.optLong("currentAccountsPayable"));
                    balanceLLM.setDeferredRevenue(balanceJson.optLong("deferredRevenue"));
                    balanceLLM.setCurrentDebt(balanceJson.optLong("currentDebt"));
                    balanceLLM.setShortTermDebt(balanceJson.optLong("shortTermDebt"));
                    balanceLLM.setTotalNonCurrentLiabilities(balanceJson.optLong("totalNonCurrentLiabilities"));
                    balanceLLM.setCapitalLeaseObligations(balanceJson.optLong("capitalLeaseObligations"));
                    balanceLLM.setLongTermDebt(balanceJson.optLong("longTermDebt"));
                    balanceLLM.setCurrentLongTermDebt(balanceJson.optLong("currentLongTermDebt"));
                    balanceLLM.setLongTermDebtNoncurrent(balanceJson.optLong("longTermDebtNoncurrent"));
                    balanceLLM.setShortLongTermDebtTotal(balanceJson.optLong("shortLongTermDebtTotal"));
                    balanceLLM.setOtherCurrentLiabilities(balanceJson.optLong("otherCurrentLiabilities"));
                    balanceLLM.setOtherNonCurrentLiabilities(balanceJson.optLong("otherNonCurrentLiabilities"));
                    balanceLLM.setTotalShareholderEquity(balanceJson.optLong("totalShareholderEquity"));
                    balanceLLM.setTreasuryStock(balanceJson.optLong("treasuryStock"));
                    balanceLLM.setRetainedEarnings(balanceJson.optLong("retainedEarnings"));
                    balanceLLM.setCommonStock(balanceJson.optLong("commonStock"));
                    balanceLLM.setCommonStockSharesOutstanding(balanceJson.optLong("commonStockSharesOutstanding"));
                    balanceLLMList.add(balanceLLM);
                }
            }
        }
        return balanceLLMList;
    }

    private List<CashFlowLLM> buildFundamentalsDataCashFlow(JSONObject value) {
        List<CashFlowLLM> cashFlowLLMList = new ArrayList<>();
        JSONArray dataArray = value.optJSONArray("annualReports");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject cashFlowJson = dataArray.optJSONObject(i);
                if (cashFlowJson != null) {
                    CashFlowLLM cashFlowLLM = new CashFlowLLM();
                    cashFlowLLM.setFiscalDateEnding(cashFlowJson.optString("fiscalDateEnding"));
                    cashFlowLLM.setReportedCurrency(cashFlowJson.optString("reportedCurrency"));
                    cashFlowLLM.setOperatingCashflow(cashFlowJson.optLong("operatingCashflow"));
                    cashFlowLLM.setPaymentsForOperatingActivities(cashFlowJson.optLong("paymentsForOperatingActivities"));
                    cashFlowLLM.setProceedsFromOperatingActivities(cashFlowJson.optString("proceedsFromOperatingActivities"));
                    cashFlowLLM.setChangeInOperatingLiabilities(cashFlowJson.optLong("changeInOperatingLiabilities"));
                    cashFlowLLM.setChangeInOperatingAssets(cashFlowJson.optLong("changeInOperatingAssets"));
                    cashFlowLLM.setDepreciationDepletionAndAmortization(cashFlowJson.optLong("depreciationDepletionAndAmortization"));
                    cashFlowLLM.setCapitalExpenditures(cashFlowJson.optLong("capitalExpenditures"));
                    cashFlowLLM.setChangeInReceivables(cashFlowJson.optLong("changeInReceivables"));
                    cashFlowLLM.setChangeInInventory(cashFlowJson.optLong("changeInInventory"));
                    cashFlowLLM.setProfitLoss(cashFlowJson.optLong("profitLoss"));
                    cashFlowLLM.setCashflowFromInvestment(cashFlowJson.optLong("cashflowFromInvestment"));
                    cashFlowLLM.setCashflowFromFinancing(cashFlowJson.optLong("cashflowFromFinancing"));
                    cashFlowLLM.setProceedsFromRepaymentsOfShortTermDebt(cashFlowJson.optLong("proceedsFromRepaymentsOfShortTermDebt"));
                    cashFlowLLM.setPaymentsForRepurchaseOfCommonStock(cashFlowJson.optString("paymentsForRepurchaseOfCommonStock"));
                    cashFlowLLM.setPaymentsForRepurchaseOfEquity(cashFlowJson.optString("paymentsForRepurchaseOfEquity"));
                    cashFlowLLM.setPaymentsForRepurchaseOfPreferredStock(cashFlowJson.optString("paymentsForRepurchaseOfPreferredStock"));
                    cashFlowLLM.setDividendPayout(cashFlowJson.optLong("dividendPayout"));
                    cashFlowLLM.setDividendPayoutCommonStock(cashFlowJson.optLong("dividendPayoutCommonStock"));
                    cashFlowLLM.setDividendPayoutPreferredStock(cashFlowJson.optString("dividendPayoutPreferredStock"));
                    cashFlowLLM.setProceedsFromIssuanceOfCommonStock(cashFlowJson.optString("proceedsFromIssuanceOfCommonStock"));
                    cashFlowLLM.setProceedsFromIssuanceOfLongTermDebtAndCapitalSecuritiesNet(cashFlowJson.optLong("proceedsFromIssuanceOfLongTermDebtAndCapitalSecuritiesNet"));
                    cashFlowLLM.setProceedsFromIssuanceOfPreferredStock(cashFlowJson.optString("proceedsFromIssuanceOfPreferredStock"));
                    cashFlowLLM.setProceedsFromRepurchaseOfEquity(cashFlowJson.optLong("proceedsFromRepurchaseOfEquity"));
                    cashFlowLLM.setProceedsFromSaleOfTreasuryStock(cashFlowJson.optString("proceedsFromSaleOfTreasuryStock"));
                    cashFlowLLM.setChangeInCashAndCashEquivalents(cashFlowJson.optString("changeInCashAndCashEquivalents"));
                    cashFlowLLM.setChangeInExchangeRate(cashFlowJson.optString("changeInExchangeRate"));
                    cashFlowLLM.setNetIncome(cashFlowJson.optLong("netIncome"));
                    cashFlowLLMList.add(cashFlowLLM);
                }
            }
        }
        return cashFlowLLMList;
    }

    private List<EarningsLLM> buildFundamentalsEarnings(JSONObject value) {
        List<EarningsLLM> earningsLLMList = new ArrayList<>();
        JSONArray dataArray = value.optJSONArray("annualEarnings");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject earningsJson = dataArray.optJSONObject(i);
                if (earningsJson != null) {
                    EarningsLLM earningsLLM = new EarningsLLM();
                    earningsLLM.setFiscalDateEnding(earningsJson.optString("fiscalDateEnding"));
                    earningsLLM.setReportedEPS(earningsJson.optDouble("reportedEPS"));
                    earningsLLMList.add(earningsLLM);
                }
            }
        }
        return earningsLLMList;
    }
}