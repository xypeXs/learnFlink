package ru.learn.flink.utils;

import org.apache.commons.lang3.StringUtils;
import ru.learn.flink.dto.InvestData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class InvestGenerator {
    private String assetCode;
    private BigDecimal initPrice;
    private BigDecimal priceAmplitude;
    private InvestData prevInvestData;
    private double[] factorArr;
    private int factorArrCurInd;
    private double factorSum;
    private final int PREV_FACTOR_ARR_SIZE = 20;

    private InvestGenerator(String assetCode, BigDecimal initPrice, BigDecimal priceAmplitude) {
        this.assetCode = assetCode;
        this.initPrice = initPrice;
        this.priceAmplitude = priceAmplitude;
        prevInvestData = new InvestData(assetCode, initPrice, LocalDateTime.now());
        factorArr = new double[PREV_FACTOR_ARR_SIZE];
        factorSum = 0;
        factorArrCurInd = -1;
    }

    public InvestData onNext() {
        return generateInvestData();
    }

    private InvestData generateInvestData() {
        BigDecimal factor = getNormalizedPriceFactor();
        BigDecimal delta = priceAmplitude.multiply(factor);
        BigDecimal newPrice = prevInvestData.getPrice()
                .add(delta)
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);

        InvestData newInvestData = new InvestData(this.assetCode, newPrice, LocalDateTime.now());
        prevInvestData = newInvestData;

        return newInvestData;
    }

    private BigDecimal getNormalizedPriceFactor() {
        double factor = ThreadLocalRandom.current().nextDouble(1.9d) - 0.9d;
        double normalizedFactor = (factor - 2 * factorSum / factorArr.length) / 3;

        factorArrCurInd = (factorArrCurInd + 1) % factorArr.length;
        factorSum -= factorArr[factorArrCurInd] - normalizedFactor;
        factorArr[factorArrCurInd] = normalizedFactor;

        return BigDecimal.valueOf(normalizedFactor);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String assetCode;
        private BigDecimal initPrice;
        private BigDecimal priceAmplitude;

        public Builder() {
            assetCode = null;
            initPrice = null;
            priceAmplitude = null;
        }

        public Builder assetCode(String assetCode) {
            this.assetCode = assetCode;
            return this;
        }

        public Builder initPrice(BigDecimal initPrice) {
            this.initPrice = initPrice;
            return this;
        }

        public Builder priceAmplitude(BigDecimal priceAmplitude) {
            this.priceAmplitude = priceAmplitude;
            return this;
        }

        public InvestGenerator build() {
            validateData();
            return new InvestGenerator(assetCode, initPrice, priceAmplitude);
        }

        private void validateData() {
            if (StringUtils.isBlank(assetCode) || initPrice == null || priceAmplitude == null)
                throw new IllegalArgumentException("Data should not be empty");

            if (initPrice.compareTo(BigDecimal.ZERO) < 0 || priceAmplitude.compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException("Prices could not be negative");
        }

    }

}
