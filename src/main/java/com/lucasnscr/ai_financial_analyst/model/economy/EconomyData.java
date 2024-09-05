package com.lucasnscr.ai_financial_analyst.model.economy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "EconomyData")
public class EconomyData {
    @Id
    private String name;
    private String date;
    private List<String> contentForLLM;
}
