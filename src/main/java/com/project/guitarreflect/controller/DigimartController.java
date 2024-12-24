package com.project.guitarreflect.controller;

import com.project.guitarreflect.service.LambdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/guitar-store")
@CrossOrigin("*")
public class DigimartController {

    @Autowired
    private LambdaService lambdaService;

    @PostMapping("/digimart-price")
    public Map<String, Object> getPrice(@RequestBody Map<String, Object> request) {
        String digimartLambdaName = "fetch_digimart_data";

        return lambdaService.invokeLambdaFunction(digimartLambdaName, request);
    }
}
