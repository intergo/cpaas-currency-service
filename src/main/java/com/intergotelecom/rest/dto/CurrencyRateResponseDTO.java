package com.intergotelecom.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CurrencyRateResponseDTO {
    @JsonProperty("currency_name")
    private String currencyName;

    @JsonProperty("base_currency_name")
    private String baseCurrencyName;

    private BigDecimal rate;
}
