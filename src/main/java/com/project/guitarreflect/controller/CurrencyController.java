package com.project.guitarreflect.controller;

import com.project.guitarreflect.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/latest")
    public Map<String,Object> getLatest() {
        return currencyService.fetchDataFromS3();
    }
}
