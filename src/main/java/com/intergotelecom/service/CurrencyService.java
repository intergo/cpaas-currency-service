package com.intergotelecom.service;

import com.intergotelecom.exception.CurrencyAlreadyExistsException;
import com.intergotelecom.mapper.CurrencyMapper;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.repository.CurrencyRepository;
import com.intergotelecom.rest.dto.CreateCurrencyRequestDTO;
import com.intergotelecom.rest.dto.CurrencyListResponseDTO;
import com.intergotelecom.rest.dto.CurrencyResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Transactional
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
      // assert currency does not exist
      currencyRepository.findByCurrencyName(dto.getCurrencyName())
          .ifPresent(existing -> {
            throw new CurrencyAlreadyExistsException(dto.getCurrencyName());
          });

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

    public List<CurrencyEntity> getCurrenciesByName(List<String> currencyNames) {
      return currencyRepository.findAvailableByName(currencyNames);
    }

    public Optional<CurrencyEntity> getCurrencyByName(String currencyName) {
      return currencyRepository.findAvailableByName(currencyName);
    }

    public Optional<CurrencyEntity> getBaseCurrencyOptional() {
      return currencyRepository.findBaseCurrency();
    }
}
