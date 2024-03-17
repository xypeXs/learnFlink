package ru.learn.flink.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class KafkaJsonDeserializer<OUT> implements KafkaRecordDeserializationSchema<OUT> {

    private ObjectMapper objectMapper;
    private final Class<OUT>  clazz;
    private final TypeInformation<OUT> typeInformation;

    public KafkaJsonDeserializer(Class<OUT> clazz) {
        this.clazz = clazz;
        typeInformation = TypeInformation.of(clazz);
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        KafkaRecordDeserializationSchema.super.open(context);
        objectMapper = JsonUtils.createObjectMapper();
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<OUT> collector) throws IOException {
        OUT deserializedObject = objectMapper.readValue(consumerRecord.value(), clazz);
        collector.collect(deserializedObject);
    }

    @Override
    public TypeInformation<OUT> getProducedType() {
        return typeInformation;
    }
}
