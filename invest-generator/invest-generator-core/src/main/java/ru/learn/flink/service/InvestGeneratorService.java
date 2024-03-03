package ru.learn.flink.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.learn.flink.dto.InvestData;
import ru.learn.flink.dto.InvestGeneratorCreateRequestDto;
import ru.learn.flink.utils.InvestGenerator;
import ru.learn.flink.utils.InvestGeneratorFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InvestGeneratorService {

    private final Logger LOG = LoggerFactory.getLogger(InvestGeneratorService.class);

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
        String data = getAssetCodeStream()
                .map(assetCode -> investGeneratorFactory.getGenerator(assetCode))
                .filter(Objects::nonNull)
                .map(InvestGenerator::onNext)
                .map(InvestData::getPrice)
                .map(BigDecimal::toString)
                .collect(Collectors.joining(","));

        if (data.isBlank())
            return;

        try {
            Files.writeString(Path.of("/home/gleb/projects/learnFlink/invest-generator/invest-generator-core/src/main/resources/data.tmp"), data + ',', StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.error("Error on scheduled task: InvestGeneratorService.generateData()", e);
        }
    }

    private Stream<String> getAssetCodeStream() {
        if (assetGeneratorSet.size() > 8)
            return assetGeneratorSet.stream().parallel();

        return assetGeneratorSet.stream();
    }

}
