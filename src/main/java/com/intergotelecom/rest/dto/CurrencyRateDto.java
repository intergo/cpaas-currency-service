package com.intergotelecom.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.experimental.SuperBuilder;

// todo remove if not needed
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CurrencyRateDto {
    private String id;

    private BigDecimal rate;

    // todo check
    private Instant effectiveAt;
}
