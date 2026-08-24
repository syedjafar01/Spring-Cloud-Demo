package com.syed.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Message {

    private final String instance;

    public Message(@Value("${service.instance.name:local}") String instance) {
        this.instance = instance;
    }

    @GetMapping("/")
    public String message() {
        return "Hello from " + instance;
    }
}
