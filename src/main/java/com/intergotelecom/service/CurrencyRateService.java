package com.intergotelecom.service;

import com.intergotelecom.mapper.CurrencyRateMapper;
import com.intergotelecom.repository.CurrencyRateRepository;
import com.intergotelecom.rest.dto.CurrencyRateResponseDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRateDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Transactional
@ApplicationScoped
@RequiredArgsConstructor
public class CurrencyRateService {
    private final
    CurrencyRateRepository currencyRateRepository;

    private final
    CurrencyRateMapper currencyRateMapper;

    private final
    CurrencyService currencyService;

    public CurrencyRateResponseDTO setCurrencyRates(
        String baseCurrency, List<UpdateCurrencyRateDTO> currencyRates) {

        var baseCurrencyEntity = currencyService.getBaseCurrencyOptional()
            .orElseThrow(() -> new NotFoundException("Base currency not found: " + baseCurrency));

        var currencyNames = currencyRates.stream()
            .map(UpdateCurrencyRateDTO::getCurrencyName)
            .distinct()
            .toList();

        var currencyEntities = currencyService.getCurrenciesByName(currencyNames);

        currencyRateRepository.persist(entity);

        return currencyRateMapper.toResponseDto(entity);
    }
}
