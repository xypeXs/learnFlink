package ru.learn.flink.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/invest-generator")
public interface InvestGeneratorController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello();
}
