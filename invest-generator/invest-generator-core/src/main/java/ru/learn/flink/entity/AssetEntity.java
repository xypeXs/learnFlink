package ru.learn.flink.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AssetEntity {

    @Id
    private UUID id;
    private String code;
    private BigDecimal lastPrice;
    private LocalDateTime updateTimestamp;
}
