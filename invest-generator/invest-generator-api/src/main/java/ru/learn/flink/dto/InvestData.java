package ru.learn.flink.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class InvestData {
    private String assetCode;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
