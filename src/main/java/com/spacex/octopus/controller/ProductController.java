package com.spacex.octopus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @RequestMapping(value = "product", method = RequestMethod.POST)
    public String batchCreate() {
        return "OK";
    }

    @RequestMapping(value = "product/mget", method = RequestMethod.GET)
    public String mget() {
        return "OK";
    }
}
