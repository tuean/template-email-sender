package com.tuean.email.templatesenderrest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {


    @GetMapping(value = "/")
    public String get() {
        return "succes";
    }

}
