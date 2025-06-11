package com.vibol.skaffold_jib_demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Service is running! , I'm dev ";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Kubernetes!";
    }
}
