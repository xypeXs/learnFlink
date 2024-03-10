package ru.rsatu.cursach;

import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import ru.learn.flink.dto.InvestData;
import ru.rsatu.cursach.utils.InvestAggregateFunction;
import ru.rsatu.cursach.utils.InvestAggregatedDataDBSinkFunction;

public class App {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<InvestData> investDataStream = env.fromSource(dataSource(), null, "INVEST DATA");

        investDataStream
                .keyBy(InvestData::getAssetCode)
                .window(SlidingEventTimeWindows.of(Time.seconds(60), Time.seconds(5)))
                .aggregate(new InvestAggregateFunction())
                .addSink(new InvestAggregatedDataDBSinkFunction());
    }

    public static KafkaSource<InvestData> dataSource() {
        KafkaSource<InvestData> investData = KafkaSource.<InvestData>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("invest_data")
                .setGroupId("invest_data_stream_processor")
                .setStartingOffsets(OffsetsInitializer.committedOffsets())
                .build();
    }
}
