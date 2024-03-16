package ru.rsatu.cursach.utils;

import lombok.Getter;
import ru.learn.flink.dto.InvestData;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
public class InvestDataAggregateAccumulator {

    private InvestAggregatedData aggregatedData;

    public InvestDataAggregateAccumulator() {
        aggregatedData = new InvestAggregatedData();
    }

    public InvestDataAggregateAccumulator(InvestAggregatedData aggregatedData) {
        this.aggregatedData = aggregatedData;
    }

    public InvestDataAggregateAccumulator accept(InvestData investData) {
        this.setMaxPrice(investData.getPrice());
        this.setMinPrice(investData.getPrice());
        this.setMaxTimestamp(investData.getTimestamp());
        this.setMinTimestamp(investData.getTimestamp());
        this.setAvgPrice(investData.getPrice());
        this.setAssetCode(investData.getAssetCode());
        return this;
    }

    public InvestDataAggregateAccumulator merge(InvestDataAggregateAccumulator acc2) {
        InvestAggregatedData aggregatedData2 = acc2.getAggregatedData();
        InvestAggregatedData mergedInvestAggregatedData = new InvestAggregatedData();

        BigInteger counterSum = aggregatedData.getCounter().add(aggregatedData2.getCounter());

        mergedInvestAggregatedData.setMaxPrice(aggregatedData.getMaxPrice().max(aggregatedData2.getMaxPrice()));
        mergedInvestAggregatedData.setMinPrice(aggregatedData.getMinPrice().min(aggregatedData2.getMinPrice()));
        mergedInvestAggregatedData.setCounter(counterSum);
        mergedInvestAggregatedData.setAvgPrice(aggregatedData.getAvgPrice().add(aggregatedData2.getAvgPrice()).divide(new BigDecimal(counterSum)));

        return new InvestDataAggregateAccumulator(mergedInvestAggregatedData);
    }

    public void setMaxPrice(BigDecimal price) {
        BigDecimal maxValue = aggregatedData.getMaxPrice().max(price);
        aggregatedData.setMaxPrice(maxValue);
    }

    public void setMinPrice(BigDecimal price) {
        BigDecimal minPrice = aggregatedData.getMinPrice().min(price);
        if (aggregatedData.getMinPrice().compareTo(BigDecimal.ZERO) < 0)
            minPrice = price;
        aggregatedData.setMinPrice(minPrice);
    }

    public void setMaxTimestamp(LocalDateTime ts) {
        LocalDateTime maxTS = aggregatedData.getMaxTimestamp().isBefore(ts) ? ts : aggregatedData.getMaxTimestamp();
        aggregatedData.setMaxTimestamp(maxTS);
    }

    public void setMinTimestamp(LocalDateTime ts) {
        LocalDateTime minTS = aggregatedData.getMinTimestamp().isAfter(ts) ? ts : aggregatedData.getMinTimestamp();
        aggregatedData.setMinTimestamp(minTS);
    }

    public void setAssetCode(String assetCode) {
        aggregatedData.setAssetCode(assetCode);
    }

    public void setAvgPrice(BigDecimal price) {
        BigDecimal oldAvgPrice = aggregatedData.getAvgPrice();
        BigDecimal oldDecimalCounter = new BigDecimal(aggregatedData.getCounter());

        BigDecimal newAvgPrice = oldAvgPrice.multiply(oldDecimalCounter).add(price).divide(oldDecimalCounter.add(BigDecimal.ONE));
        BigInteger newCounter = aggregatedData.getCounter().add(BigInteger.ONE);

        aggregatedData.setAvgPrice(newAvgPrice);
        aggregatedData.setCounter(newCounter);
    }
}
