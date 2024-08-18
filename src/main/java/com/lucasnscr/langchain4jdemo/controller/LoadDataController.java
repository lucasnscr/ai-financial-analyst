package com.lucasnscr.langchain4jdemo.controller;

import com.lucasnscr.langchain4jdemo.service.DataLoadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;

@RestController
public class LoadDataController {

    private final DataLoadingService dataLoadingService;

    @Autowired
    public LoadDataController(DataLoadingService dataLoadingService){
        this.dataLoadingService = dataLoadingService;
    }

    @GetMapping("/load")
    public ResponseEntity<HttpStatus> getQuestion(){
        dataLoadingService.loadData();
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
