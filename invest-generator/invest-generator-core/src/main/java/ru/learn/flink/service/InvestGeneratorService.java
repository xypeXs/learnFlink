package ru.learn.flink.service;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import ru.learn.flink.utils.InvestGeneratorFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class InvestGeneratorService {

    private InvestGeneratorFactory investGeneratorFactory;

    private List<String> assetCodeList;

    @PostConstruct
    public void init() {
        assetCodeList = new ArrayList<>();
        investGeneratorFactory = new InvestGeneratorFactory();
    }

}
