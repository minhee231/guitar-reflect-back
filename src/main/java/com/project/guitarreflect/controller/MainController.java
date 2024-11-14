package com.project.guitarreflect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class MainController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
