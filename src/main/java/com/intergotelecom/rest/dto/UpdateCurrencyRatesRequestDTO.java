package com.intergotelecom.rest.dto;

import jakarta.validation.Valid;
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
    @Valid
    @NotEmpty(message = "Currency rate list cannot be empty")
    private List<UpdateCurrencyRateDTO> currencyRates;
}
