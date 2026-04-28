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

    public CurrencyRateResponseDTO setCurrencyRate(UpdateCurrencyRateRequestDTO dto) {
        CurrencyEntity currency = currencyRepository.findByCurrencyName(dto.getCurrencyName())
            .orElseThrow(() -> new NotFoundException("Currency not found: " + dto.getCurrencyName()));

        CurrencyRateEntity entity = currencyRateMapper.toEntity(dto);
        entity.setCurrencyId(currency.id);

        currencyRateRepository.persist(entity);

        return currencyRateMapper.toResponseDto(entity);
    }
}
