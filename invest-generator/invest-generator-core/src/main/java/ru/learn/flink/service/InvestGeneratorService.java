package ru.learn.flink.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.learn.flink.dto.InvestGeneratorCreateRequestDto;
import ru.learn.flink.utils.InvestGenerator;
import ru.learn.flink.utils.InvestGeneratorFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Singleton
public class InvestGeneratorService {

    private final Logger LOG = LoggerFactory.getLogger(InvestGeneratorService.class);

    @Inject
    KafkaJsonProducerService kafkaJsonProducerService;

    private InvestGeneratorFactory investGeneratorFactory;

    private Set<String> assetGeneratorSet;

    @PostConstruct
    public void init() {
        assetGeneratorSet = new HashSet<>();
        investGeneratorFactory = new InvestGeneratorFactory();
    }

    public InvestGeneratorCreateRequestDto register(InvestGeneratorCreateRequestDto requestDto) {
        investGeneratorFactory.getOrCreateGenerator(requestDto.getAssetCode(), requestDto.getInitPrice(), requestDto.getPriceAmplitude());
        assetGeneratorSet.add(requestDto.getAssetCode());
        return requestDto;
    }

    @Scheduled(every = "1s")
    public void generateData() {
        getAssetCodeStream()
                .map(investGeneratorFactory::getGenerator)
                .filter(Objects::nonNull)
                .map(InvestGenerator::onNext)
                .forEach(kafkaJsonProducerService::sendMessage);
    }

    private Stream<String> getAssetCodeStream() {
        if (assetGeneratorSet.size() > 8)
            return assetGeneratorSet.stream().parallel();

        return assetGeneratorSet.stream();
    }

}
