package com.intergotelecom.service;

import com.intergotelecom.mapper.CurrencyMapper;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.repository.CurrencyRepository;
import com.intergotelecom.rest.dto.CreateCurrencyRequestDTO;
import com.intergotelecom.rest.dto.CurrencyListResponseDTO;
import com.intergotelecom.rest.dto.CurrencyResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
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

        String baseCurrencyName = getBaseCurrencyOptional()
            .map(CurrencyEntity::getCurrencyName)
            .orElse(null);

        return CurrencyListResponseDTO.builder()
            .baseCurrency(baseCurrencyName)
            .currencies(currencies)
            .build();
    }

    public CurrencyResponseDTO createCurrency(CreateCurrencyRequestDTO dto) {
      // only 1 currency should be set as base
      if (dto.isBaseCurrency()) {
        getBaseCurrencyOptional().ifPresent(
            currencyEntity -> currencyEntity.setBaseCurrency(false));
      }

      // convert to entity and persist
      CurrencyEntity entity = currencyMapper.toEntity(dto);
      currencyRepository.persist(entity);
      return currencyMapper.toResponseDto(entity);
    }

    private Optional<CurrencyEntity> getBaseCurrencyOptional() {
      return currencyRepository.findBaseCurrency();
    }
}
