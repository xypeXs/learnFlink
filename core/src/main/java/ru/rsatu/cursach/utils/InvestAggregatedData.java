package ru.rsatu.cursach.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InvestAggregatedData {

    private String assetCode = "";
    private BigDecimal maxPrice = BigDecimal.ZERO;
    private BigDecimal minPrice = BigDecimal.ZERO;
    private BigDecimal avgPrice = BigDecimal.ZERO;
    private BigInteger counter = BigInteger.ZERO;
    private LocalDateTime minTimestamp = LocalDateTime.MAX;
    private LocalDateTime maxTimestamp = LocalDateTime.MIN;
}
