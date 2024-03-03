package ru.learn.flink.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ru.learn.flink.dto.InvestGeneratorCreateRequestDto;

@Path("/invest")
public interface InvestGeneratorController {

    @POST
    @Path(("/register"))
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InvestGeneratorCreateRequestDto register(InvestGeneratorCreateRequestDto requestDto);
}
