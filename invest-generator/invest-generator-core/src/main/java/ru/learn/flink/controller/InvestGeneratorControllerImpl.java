package ru.learn.flink.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


public class InvestGeneratorControllerImpl implements InvestGeneratorController {

    @Override
    public String hello() {
        return "Hello RESTEasy";
    }
}
