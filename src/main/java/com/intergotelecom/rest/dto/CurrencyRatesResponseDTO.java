package com.intergotelecom.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CurrencyRatesResponseDTO {
    @JsonProperty("base_currency_name")
    private String baseCurrencyName;

    @JsonProperty("currency_rates")
    private List<CurrencyRateResponseDTO> currencyRates;
}
