package com.lucasnscr.ai_financial_analyst.controller;

import com.lucasnscr.ai_financial_analyst.service.DataLoadingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoadDataController {

    private final DataLoadingService dataLoadingService;

    public LoadDataController(DataLoadingService dataLoadingService){
        this.dataLoadingService = dataLoadingService;
    }

    @GetMapping("/load")
    public ResponseEntity<HttpStatus> getQuestion(){
        dataLoadingService.loadData();
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
