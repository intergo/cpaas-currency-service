package com.intergotelecom.service;

import com.intergotelecom.mapper.CurrencyMapper;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.repository.CurrencyRepository;
import com.intergotelecom.rest.dto.CreateCurrencyRequestDTO;
import com.intergotelecom.rest.dto.CurrencyListResponseDTO;
import com.intergotelecom.rest.dto.CurrencyResponseDTO;
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

    public CurrencyListResponseDTO getCurrencies() {
        List<CurrencyResponseDTO> currencies = currencyRepository.findAvailable().stream()
            .map(currencyMapper::toResponseDto)
            .toList();

        String baseCurrencyName = currencyRepository.findBaseCurrency()
            .map(CurrencyEntity::getCurrencyName)
            .orElse(null);

        return CurrencyListResponseDTO.builder()
            .baseCurrency(baseCurrencyName)
            .currencies(currencies)
            .build();
    }

    public CurrencyResponseDTO createCurrency(CreateCurrencyRequestDTO dto) {
      CurrencyEntity entity = currencyMapper.toEntity(dto);
      currencyRepository.persist(entity);
      return currencyMapper.toResponseDto(entity);
    }
}
