package com.lucasnscr.ai_financial_analyst.model.fundamentals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "FundamentalsDataCompany")
public class FundamentalsDataCompany {

    @Id
    private String name;
    private String date;
    private List<String> contentForLLM;

}
