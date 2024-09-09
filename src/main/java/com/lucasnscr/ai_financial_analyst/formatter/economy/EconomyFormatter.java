package com.lucasnscr.ai_financial_analyst.formatter.economy;

import com.lucasnscr.ai_financial_analyst.llm.model.economy.*;
import org.springframework.stereotype.Component;

@Component
public class EconomyFormatter {
    public String formatEconomy(EconomyLLM economyLLM) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
        Real Gross Domestic Product
        Interval: annual
        Unit: billions of dollars
        """);
        economyLLM.getGdpYearLLMList()
                .stream()
                .map(this::formatGdpYear)
                .forEach(sb::append);

        return sb.toString();
    }

    public String formatTreasury(EconomyLLM economyLLM) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
        10-Year Treasury Constant Maturity Rate
        Interval: monthly
        Unit: percent
        """);
        economyLLM.getTreasureLLMList()
                .stream()
                .map(this::formatTreasuryMonthly)
                .forEach(sb::append);
        return sb.toString();
    }

    public String formatFundsRate(EconomyLLM economyLLM){
        StringBuilder sb = new StringBuilder();
        sb.append("""
        Effective Federal Funds Rate
        Interval: monthly
        Unit: percent
        """);
        economyLLM.getFederalFundsRateLLMList()
                .stream()
                .map(this::formatFederalFundsRate)
                .forEach(sb::append);

        return sb.toString();
    }

    public String formatUnployment(EconomyLLM economyLLM){
        StringBuilder sb = new StringBuilder();
        sb.append("""
        Unemployment Rate
        Interval: monthly
        Unit: percent
        """);
        economyLLM.getUnemploymentLLMList()
                .stream()
                .map(this::formatUnemployment)
                .forEach(sb::append);
        return sb.toString();
    }

    public String formatInflation(EconomyLLM economyLLM){
        StringBuilder sb = new StringBuilder();
        sb.append("""
        Inflation - US Consumer Prices
        Interval: annual
        Unit: percent
        """);
        economyLLM.getInflationLLMList()
                .stream()
                .map(this::formatInflation)
                .forEach(sb::append);
        return sb.toString();
    }

    private String formatGdpYear(GdpYearLLM gdpYearLLM) {
        return String.format("""
                    Gross Domestic Product date: %s
                    Gross Domestic Product result value: %.3f
                    """,
                gdpYearLLM.getDate(), gdpYearLLM.getValue());
    }

    private String formatFederalFundsRate(FederalFundsRateLLM federalFundsRateLLM) {
        return String.format("""
                    Federal Funds Rate date: %s
                    Federal Funds Rate value: %.3f
                    """,
                federalFundsRateLLM.getDate(), federalFundsRateLLM.getValue());
    }

    private String formatTreasuryMonthly(TreasureLLM treasureLLM) {
        return String.format("""
                    Treasury Constant Maturity Rate date: %s
                    Treasury Constant Maturity Rate value: %.3f
                    """,
                treasureLLM.getDate(), treasureLLM.getValue());
    }

    private String formatUnemployment(UnemploymentLLM unemploymentLLM) {
        return String.format("""
                    Unemployment Rate date: %s
                    Unemployment Rate value: %.3f
                    """,
                unemploymentLLM.getDate(), unemploymentLLM.getValue());
    }

    private String formatInflation(InflationLLM inflationLLM) {
        return String.format("""
                    Inflation - US Consumer Prices date: %s
                    Inflation Rate value: %.3f
                    """,
                inflationLLM.getDate(), inflationLLM.getValue());
    }
}