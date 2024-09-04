package com.lucasnscr.ai_financial_analyst.model.classification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("StockClassifications")
public class StockClassification {

    @Id
    private String date;
    private String name;
    private List<String> contentForLLM;
}
