package com.intergotelecom.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UpdateCurrencyRatesRequestDTO {
    @NotBlank(message = "Base currency name cannot be blank")
    @JsonProperty("base_currency_name")
    private String baseCurrencyName;

    @Valid
    @NotEmpty(message = "Currency rate list cannot be empty")
    private List<UpdateCurrencyRateDTO> currencyRates;
}
