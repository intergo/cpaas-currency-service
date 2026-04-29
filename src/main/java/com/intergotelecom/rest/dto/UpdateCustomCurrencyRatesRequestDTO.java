package com.intergotelecom.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UpdateCustomCurrencyRatesRequestDTO {
    @NotBlank(message = "Base currency name cannot be blank")
    @JsonProperty("base_currency_name")
    private String baseCurrencyName;

    @Valid
    @NotNull(message = "Rate dto cannot be null")
    private UpdateCurrencyRateDTO currencyRateDTO;
}
