package com.lucasnscr.ai_financial_analyst.llm.model.economy;

import lombok.Data;

import java.util.List;

@Data
public class EconomyLLM {
    List<GdpYearLLM> gdpYearLLMList;
    List<TreasureLLM> treasureLLMList;
    List<FederalFundsRateLLM> federalFundsRateLLMList;
    List<UnemploymentLLM> unemploymentLLMList;
    List<InflationLLM> inflationLLMList;
}
