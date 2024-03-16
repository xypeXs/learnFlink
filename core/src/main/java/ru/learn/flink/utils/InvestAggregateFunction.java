package ru.learn.flink.utils;

import org.apache.flink.api.common.functions.AggregateFunction;
import ru.learn.flink.dto.InvestData;

public class InvestAggregateFunction implements AggregateFunction<InvestData, InvestDataAggregateAccumulator, InvestAggregatedData> {

    @Override
    public InvestDataAggregateAccumulator createAccumulator() {
        return new InvestDataAggregateAccumulator();
    }

    @Override
    public InvestDataAggregateAccumulator add(InvestData investData, InvestDataAggregateAccumulator investDataAggregateAccumulator) {
        return investDataAggregateAccumulator.accept(investData);
    }

    @Override
    public InvestAggregatedData getResult(InvestDataAggregateAccumulator investDataAggregateAccumulator) {
        return investDataAggregateAccumulator.getAggregatedData();
    }

    @Override
    public InvestDataAggregateAccumulator merge(InvestDataAggregateAccumulator investDataAggregateAccumulator1, InvestDataAggregateAccumulator investDataAggregateAccumulator2) {
        return investDataAggregateAccumulator1.merge(investDataAggregateAccumulator2);
    }
}
