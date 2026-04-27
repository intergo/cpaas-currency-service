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
public class CurrencyListResponseDTO {
    @JsonProperty("base_currency")
    private String baseCurrency;

    private List<CurrencyResponseDTO> currencies;
}
