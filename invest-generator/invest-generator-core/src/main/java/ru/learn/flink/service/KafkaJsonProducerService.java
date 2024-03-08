package ru.learn.flink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import ru.learn.flink.data.KafkaConstant;

@ApplicationScoped
public class KafkaJsonProducerService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @Channel(KafkaConstant.Topic.INVEST_DATA)
    @OnOverflow(value = OnOverflow.Strategy.DROP, bufferSize = 1024)
    Emitter<String> kafkaMessageEmitter;


    public void sendMessage(Object message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            kafkaMessageEmitter.send(messageJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
