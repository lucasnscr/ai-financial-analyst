package com.lucasnscr.ai_financial_analyst.llm.model.fundamentals;

import lombok.Data;

import java.util.List;

@Data
public class FundamentalsDataCompanyLLM {

    private String symbol;
    private String assetType;
    private String name;
    private String description;
    private String cik;
    private String exchange;
    private String currency;
    private String country;
    private String sector;
    private String industry;
    private String address;
    private String officialSite;
    private String fiscalYearEnd;
    private String latestQuarter;
    private long marketCapitalization;
    private long ebitda;
    private double peRatio;
    private double pegRatio;
    private double bookValue;
    private double dividendPerShare;
    private double dividendYield;
    private double eps;
    private double revenuePerShareTTM;
    private double profitMargin;
    private double operatingMarginTTM;
    private double returnOnAssetsTTM;
    private double returnOnEquityTTM;
    private long revenueTTM;
    private long grossProfitTTM;
    private double dilutedEPSTTM;
    private double quarterlyEarningsGrowthYOY;
    private double quarterlyRevenueGrowthYOY;
    private double analystTargetPrice;
    private int analystRatingStrongBuy;
    private int analystRatingBuy;
    private int analystRatingHold;
    private int analystRatingSell;
    private int analystRatingStrongSell;
    private double trailingPE;
    private double forwardPE;
    private double priceToSalesRatioTTM;
    private double priceToBookRatio;
    private double evToRevenue;
    private double evToEBITDA;
    private double beta;
    private double week52High;
    private double week52Low;
    private double movingAverage50Day;
    private double movingAverage200Day;
    private long sharesOutstanding;
    private String dividendDate;
    private String exDividendDate;
    private List<DividendsLLM> dividendsLLMList;
    private List<SplitLLM> splitLLMList;
    private List<IncomeLLM> incomeLLMList;
    private List<BalanceLLM> balanceLLMList;
    private List<CashFlowLLM> cashFlowLLMList;
    private List<EarningsLLM> earningsLLMList;
}
