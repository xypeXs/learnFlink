package ru.rsatu.cursach.utils;

import lombok.Getter;
import ru.learn.flink.dto.InvestData;

import java.math.BigDecimal;
import java.math.BigInteger;

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
        this.setAvgPrice(investData.getPrice());
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
        if (aggregatedData.getMaxPrice().compareTo(BigDecimal.ZERO) > 0)
            return;
        aggregatedData.setMaxPrice(price);
    }

    public void setMinPrice(BigDecimal price) {
        if (aggregatedData.getMinPrice().compareTo(BigDecimal.ZERO) > 0)
            return;
        aggregatedData.setMinPrice(price);
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
