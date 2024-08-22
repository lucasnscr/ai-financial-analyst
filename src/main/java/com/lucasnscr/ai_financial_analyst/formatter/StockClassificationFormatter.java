package com.lucasnscr.ai_financial_analyst.formatter;

import com.lucasnscr.ai_financial_analyst.llm.model.StockClassficationLLM;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class StockClassificationFormatter {

    private final Map<String, Function<StockClassficationLLM, String>> classificationStrategies = Map.of(
            "top_gainers", this::formatTopGainers,
            "top_losers", this::formatTopLosers,
            "most_actively_traded", this::formatMostActivelyTraded
    );

    public String format(String type, StockClassficationLLM classification) {
        return classificationStrategies.getOrDefault(type, this::formatUnknown)
                .apply(classification);
    }

    private String formatTopGainers(StockClassficationLLM classification) {
        return String.format("""
                        Top Gainers US tickers
                        Ticker: %s
                        Price: %.2f
                        Change Amount: %.2f
                        Change Percentage: %s
                        Volume: %.2f
                        """,
                classification.getTicker(), classification.getPrice(),
                classification.getChangeAmount(), classification.getChangePercentage(),
                classification.getVolume());
    }

    private String formatTopLosers(StockClassficationLLM classification) {
        return String.format("""
                        Top Losers US tickers
                        Ticker: %s
                        Price: %.2f
                        Change Amount: %.2f
                        Change Percentage: %s
                        Volume: %.2f
                        """,
                classification.getTicker(), classification.getPrice(),
                classification.getChangeAmount(), classification.getChangePercentage(),
                classification.getVolume());
    }

    private String formatMostActivelyTraded(StockClassficationLLM classification) {
        return String.format("""
                        Most Actively Traded US tickers
                        Ticker: %s
                        Price: %.2f
                        Change Amount: %.2f
                        Change Percentage: %s
                        Volume: %.2f
                        """,
                classification.getTicker(), classification.getPrice(),
                classification.getChangeAmount(), classification.getChangePercentage(),
                classification.getVolume());
    }

    private String formatUnknown(StockClassficationLLM classification) {
        return "Unknown classification type";
    }

}
