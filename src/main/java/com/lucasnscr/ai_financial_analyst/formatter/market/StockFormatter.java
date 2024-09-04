package com.lucasnscr.ai_financial_analyst.formatter.market;

import com.lucasnscr.ai_financial_analyst.llm.model.market.StockLLM;
import org.springframework.stereotype.Component;

@Component
public class StockFormatter {

    public String format(String name, StockLLM stockLLM) {
        return String.format("""
                        Stock Information for: %s
                        Date: %s
                        Open Price: %s
                        High Price: %s
                        Low Price: %s
                        Close Price: %s
                        Adjusted Close Price: %s
                        Volume: %s
                        Dividend Amount: %s
                        Coefficient: %s
                        """,
                name, stockLLM.getDate(), stockLLM.getPriceOpen(), stockLLM.getPriceHigh(),
                stockLLM.getPriceLow(), stockLLM.getPriceClose(), stockLLM.getPriceAdjustedClose(),
                stockLLM.getVolume(), stockLLM.getDividendAmount(), stockLLM.getCoefficient());
    }
}
