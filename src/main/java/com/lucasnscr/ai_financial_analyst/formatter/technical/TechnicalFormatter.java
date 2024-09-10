package com.lucasnscr.ai_financial_analyst.formatter.technical;

import com.lucasnscr.ai_financial_analyst.llm.model.technical.BbandsLLM;
import com.lucasnscr.ai_financial_analyst.llm.model.technical.MacdLLM;
import com.lucasnscr.ai_financial_analyst.llm.model.technical.TechnicalDataLLM;
import org.springframework.stereotype.Component;

@Component
public class TechnicalFormatter {

    public String formatTechnical(String name, TechnicalDataLLM technicalDataLLM){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("""
                    Relative Strength Index (RSI)
                  """);
        technicalDataLLM.getRsiList()
                .stream()
                .map(this::formatRsi)
                .forEach(sb::append);
        sb.append("""
                     Bollinger Bands (BBANDS)"
                  """);
        technicalDataLLM.getBbandsLLMList()
                .stream()
                .map(this::formatBbands)
                .forEach(sb::append);
        sb.append(name);
        sb.append("""
                    Moving Average Convergence/Divergence (MACD)
                """);
        technicalDataLLM.getMacdLLMList()
                .stream()
                .map(this::formatMacd)
                .forEach(sb::append);

        return sb.toString();
    }

    private String formatMacd(MacdLLM macdLLM) {
        return String.format("""
                        "MACD": %s
                        "MACD_Signal": %s
                        "MACD_Hist": %s
                        """,macdLLM.getMacd(), macdLLM.getMacdSignal(), macdLLM.getMacdHist());
    }

    private Object formatBbands(BbandsLLM bbandsLLM) {
        return String.format("""
                    Real Upper Band": %s
                    Real Middle Band": %s
                    Real Lower Band": %s
                    """,bbandsLLM.getRealUpperBand(), bbandsLLM.getRealMiddleBand(), bbandsLLM.getRealLowerBand());
    }

    private String formatRsi(Double aDouble) {
        return String.format("""
                        RSI: %s
                        """,aDouble);
    }
}
