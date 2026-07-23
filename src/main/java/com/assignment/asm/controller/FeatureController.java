package com.assignment.asm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeatureController {

    @GetMapping("/features")
    public String features() {
        return "features";
    }
}
