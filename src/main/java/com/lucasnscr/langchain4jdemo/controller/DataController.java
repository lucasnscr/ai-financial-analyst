package com.lucasnscr.langchain4jdemo.controller;

import com.lucasnscr.langchain4jdemo.service.DataLoadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataLoadingService dataLoadingService;

    @Autowired
    public DataController(DataLoadingService dataLoadingService) {
        this.dataLoadingService = dataLoadingService;
    }

    @GetMapping("/load/{stock}")
    public ResponseEntity<String> load(@PathVariable("stock") String ticker) {
        try {
            this.dataLoadingService.loadData(ticker);
            return ResponseEntity.ok("Data loaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while loading data: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred in the controller: " + e.getMessage());
    }
}
