package com.me.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class HelloController {
    @GetMapping("/hello")
    public Object hello() {
        return "Hello World~";
    }
}
