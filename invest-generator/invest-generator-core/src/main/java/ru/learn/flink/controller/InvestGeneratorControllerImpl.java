package ru.learn.flink.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import ru.learn.flink.dto.InvestGeneratorCreateRequestDto;
import ru.learn.flink.service.InvestGeneratorService;

@Path("/invest")
public class InvestGeneratorControllerImpl implements InvestGeneratorController {

    @Inject
    InvestGeneratorService investGeneratorService;

    @Override
    public InvestGeneratorCreateRequestDto register(InvestGeneratorCreateRequestDto requestDto) {
        return investGeneratorService.register(requestDto);
    }
}
