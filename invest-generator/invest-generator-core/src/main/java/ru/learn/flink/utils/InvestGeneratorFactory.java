package ru.learn.flink.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InvestGeneratorFactory {

    private final Map<String, InvestGenerator> investGeneratorMap;

    public InvestGeneratorFactory() {
        investGeneratorMap = new HashMap<>();
    }

    public synchronized InvestGenerator getGenerator(String assetCode, BigDecimal initPrice, BigDecimal priceAmplitude) {
        if (!investGeneratorMap.containsKey(assetCode)) {
            InvestGenerator newInvestGenerator = InvestGenerator.builder()
                    .assetCode(assetCode)
                    .initPrice(initPrice)
                    .priceAmplitude(priceAmplitude)
                    .build();
            investGeneratorMap.put(assetCode, newInvestGenerator);
        }
        return investGeneratorMap.get(assetCode);
    }
}
