package ru.learn.flink.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestGeneratorCreateRequestDto {
    private String assetCode;
    private BigDecimal initPrice;
    private BigDecimal priceAmplitude;
}
