package ru.learn.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import ru.learn.flink.dto.InvestData;
import ru.learn.flink.utils.InvestAggregateFunction;
import ru.learn.flink.utils.InvestAggregatedDataDBSinkFunction;
import ru.learn.flink.utils.KafkaJsonDeserializer;

public class Main {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<InvestData> investDataStream = env.fromSource(dataSource(), WatermarkStrategy.forMonotonousTimestamps(), "INVEST DATA");

        investDataStream
                .keyBy(InvestData::getAssetCode)
                .window(SlidingEventTimeWindows.of(Time.seconds(60), Time.seconds(5)))
                .aggregate(new InvestAggregateFunction())
                .addSink(new InvestAggregatedDataDBSinkFunction(60000L));

        env.execute();
    }

    public static KafkaSource<InvestData> dataSource() {
        KafkaSource<InvestData> investDataSource = KafkaSource.<InvestData>builder()
                .setBootstrapServers("kafka:9002")
                .setDeserializer(new KafkaJsonDeserializer<>(InvestData.class))
                .setTopics("invest_data")
                .setGroupId("invest_data_stream_processor")
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.LATEST))
                .build();
        return investDataSource;
    }
}
