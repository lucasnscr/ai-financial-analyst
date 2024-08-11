package com.lucasnscr.langchain4jdemo.validations;

import com.lucasnscr.langchain4jdemo.exception.StockNotAvailableException;
import com.lucasnscr.langchain4jdemo.model.CompanyStock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class StockAnalyzer {
    public void analyzeStock(String ticker) throws StockNotAvailableException {
        boolean exists = false;
        if (!StringUtils.isBlank(ticker)){
            for (CompanyStock stockTicker : CompanyStock.values()) {
                if (stockTicker.getTicker().equalsIgnoreCase(ticker)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                throw new StockNotAvailableException(HttpStatus.NOT_FOUND, "Stock " + ticker + " not available for analysis.");
            }
        }else {
            throw new StockNotAvailableException(HttpStatus.BAD_REQUEST, "Stock is null or empty");
        }

    }
}
