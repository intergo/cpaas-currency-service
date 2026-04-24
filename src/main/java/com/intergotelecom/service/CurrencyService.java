package com.intergotelecom.service;

import com.intergotelecom.mapper.CurrencyMapper;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.repository.CurrencyRepository;
import com.intergotelecom.rest.dto.CurrencyDto;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CurrencyService {
    private final
    CurrencyRepository currencyRepository;

    private final
    CurrencyMapper currencyMapper;

    public List<CurrencyDto> getCurrencies() {
        List<CurrencyEntity> entities = currencyRepository.findAvailable();

        return entities.stream()
            .map(currencyMapper::toDto)
            .toList();
    }
}
