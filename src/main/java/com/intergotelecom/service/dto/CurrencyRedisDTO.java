package com.intergotelecom.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CurrencyRedisDTO {
    private String baseCurrency;
    private String currency;
    private String rate;
}
