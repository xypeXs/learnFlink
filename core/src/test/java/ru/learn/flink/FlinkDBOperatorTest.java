package ru.learn.flink;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.learn.flink.utils.InvestAggregatedData;
import ru.learn.flink.utils.InvestAggregatedDataDBSinkFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


public class FlinkDBOperatorTest {

    @Test
    @Ignore
    public void testInvestSlideSaveOperator() {
        InvestAggregatedDataDBSinkFunction sink = new InvestAggregatedDataDBSinkFunction(60000L);

        for (int i = 0; i < 100; i++) {
            InvestAggregatedData data = createRandomInvestAggregatedData();

            try {
                sink.invoke(data, null);
            } catch (Exception e) {
                Assert.fail();
            }
        }
    }

    private InvestAggregatedData createRandomInvestAggregatedData() {
        InvestAggregatedData investData = new InvestAggregatedData();

        String[] assetCodeArr = {"SBER", "TCS", "AUQA"};

        investData.setMaxTimestamp(LocalDateTime.now());
        investData.setMinTimestamp(LocalDateTime.now().minusSeconds(60));
        investData.setCounter(BigInteger.valueOf(ThreadLocalRandom.current().nextLong(10L, 100L)));
        investData.setAvgPrice(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10.0d, 1000.0d)));
        investData.setMinPrice(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10.0d, 1000.0d)));
        investData.setMaxPrice(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10.0d, 1000.0d)));
        investData.setAssetCode(assetCodeArr[ThreadLocalRandom.current().nextInt(0, assetCodeArr.length)]);

        return investData;
    }
}
