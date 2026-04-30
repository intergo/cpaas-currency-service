package com.intergotelecom.service;

import com.intergotelecom.enums.RateProviderEnum;
import com.intergotelecom.enums.RedisKeys;
import com.intergotelecom.mapper.CurrencyRateMapper;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.model.CurrencyRateEntity;
import com.intergotelecom.repository.CurrencyRateRepository;
import com.intergotelecom.rest.dto.CurrencyRatesResponseDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRateDTO;
import com.intergotelecom.service.dto.CurrencyDomainDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

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

    private final
    RedisService<CurrencyDomainDTO> redisService;

    @ConfigProperty(name = "currency.rate.cache.ttl", defaultValue = "3600")
    private long cacheTtlSeconds;

    @ConfigProperty(name = "app.base-currency", defaultValue = "EUR")
    private String baseCurrencyName;

    public CurrencyRatesResponseDTO setCurrencyRates(List<UpdateCurrencyRateDTO> currencyRatesDTOs) {
        // create a map with rate DTOs
        Map<String, UpdateCurrencyRateDTO> currencyRatesMap = currencyRatesDTOs.stream()
            .collect(Collectors.toMap(
                UpdateCurrencyRateDTO::getCurrencyName,
                Function.identity()));

        Set<String> currenciesToUpdate = currencyRatesMap.keySet();

        // fetch existing rate entities
        List<CurrencyRateEntity> rateEntities = getCurrencyRates(
            baseCurrencyName, RateProviderEnum.ECB, currenciesToUpdate);

        List<String> currenciesWithRate = rateEntities.stream()
            .map(rateEntity ->
                rateEntity.getCurrency().getCurrencyName())
            .toList();

        // update rates for existing entities
        rateEntities.forEach(rateEntity -> {
            var rateDTO = currencyRatesMap.get(
                rateEntity.getCurrency().getCurrencyName());
            rateEntity.setRate(rateDTO.getRate());
        });

        // find currencies with missing rates
        List<String> missingCurrencyNames = currenciesToUpdate.stream()
            .filter(name -> !currenciesWithRate.contains(name))
            .toList();

        // fetch currency entities for missing rates
        List<CurrencyEntity> missingCurrencies =
            currencyService.getCurrenciesByName(missingCurrencyNames);

        if (missingCurrencies.isEmpty()) {
            // cache currency rates to redis
            List<CurrencyDomainDTO> domainDTOs = currencyRateMapper
                .toDomainDTO(rateEntities);

            cacheRate(domainDTOs);

            return currencyRateMapper.toResponseDto(baseCurrencyName, domainDTOs);
        }

        // fetch base currency
        CurrencyEntity baseCurrencyEntity = currencyService.getBaseCurrencyOptional()
            .orElseThrow(() -> new NotFoundException("Base currency not found: " + baseCurrencyName));

        // create new rate entities
        List<CurrencyRateEntity> newRateEntities = missingCurrencies.stream()
            .map(currencyEntity -> {
              UpdateCurrencyRateDTO rateDTO = currencyRatesMap.get(currencyEntity.getCurrencyName());
              return currencyRateMapper.toEntity(
                  rateDTO, RateProviderEnum.ECB, currencyEntity, baseCurrencyEntity);
          })
          .toList();

        // persist new entities
        currencyRateRepository.persist(newRateEntities);

        // create response and return
        rateEntities.addAll(newRateEntities);

        // cache currency rates to redis
        List<CurrencyDomainDTO> domainDTOs = currencyRateMapper.toDomainDTO(rateEntities);

        cacheRate(domainDTOs);

        return currencyRateMapper.toResponseDto(baseCurrencyName, domainDTOs);
    }

    public CurrencyRatesResponseDTO getCurrencyRatesResponse(List<String> requestedCurrencies) {
      List<CurrencyDomainDTO> cachedDTOList = requestedCurrencies.stream()
          .map(currency -> {
            String key = RedisKeys.createCurrencyKey(baseCurrencyName, currency);
            return redisService.getCachedObject(key);
          })
          .filter(Optional::isPresent)
          .map(Optional::get)
          .collect(Collectors.toList());

      List<String> foundCurrencies = cachedDTOList.stream()
          .map(CurrencyDomainDTO::getCurrency)
          .toList();

      List<String> missingCurrencies = requestedCurrencies.stream()
          .filter(currency -> !foundCurrencies.contains(currency))
          .toList();

      if (missingCurrencies.isEmpty()) {
        return currencyRateMapper.toResponseDto(baseCurrencyName, cachedDTOList);
      }

      List<CurrencyRateEntity> rateEntities = getCurrencyRates(
          baseCurrencyName, RateProviderEnum.ECB, missingCurrencies);

      // cache currency rates to redis
      List<CurrencyDomainDTO> domainDTOs = currencyRateMapper
          .toDomainDTO(rateEntities);

      cacheRate(domainDTOs);

      cachedDTOList.addAll(domainDTOs);

      return currencyRateMapper.toResponseDto(baseCurrencyName, cachedDTOList);
    }

    public CurrencyRatesResponseDTO setCustomRates(UpdateCurrencyRateDTO currencyRateDTO) {
      CurrencyEntity baseCurrencyEntity = currencyService.getBaseCurrencyOptional()
          .orElseThrow(() -> new NotFoundException("Base currency not found: " + baseCurrencyName));

      Optional<CurrencyRateEntity> rateEntityOptional = getCurrencyRate(
          baseCurrencyName, currencyRateDTO.getCurrencyName(), RateProviderEnum.CUSTOM);

      CurrencyRateEntity rateEntity = rateEntityOptional.map(
              existingEntity -> {
                existingEntity.setRate(currencyRateDTO.getRate());
                return existingEntity;
              })
          .orElseGet(() -> {
            CurrencyEntity currencyEntity = currencyService
                .getCurrencyByName(currencyRateDTO.getCurrencyName())
                .orElseThrow(() ->
                    new NotFoundException("Currency not found: " + currencyRateDTO.getCurrencyName()));

            CurrencyRateEntity newRateEntity = currencyRateMapper.toEntity(
                currencyRateDTO, RateProviderEnum.CUSTOM, currencyEntity, baseCurrencyEntity);

            currencyRateRepository.persist(newRateEntity);

            return newRateEntity;
          });

      CurrencyDomainDTO domainDTO = currencyRateMapper.toDomainDTO(rateEntity);

      // update the cache with the custom rate
      cacheRate(domainDTO);

      return currencyRateMapper.toResponseDto(baseCurrencyName, List.of(domainDTO));
    }

    private List<CurrencyRateEntity> getCurrencyRates(
        String baseCurrency, RateProviderEnum rateProvider, Set<String> currencyNames) {
      return currencyRateRepository.findByCurrencyBaseCurrencyAndProvider(
          baseCurrency, currencyNames, rateProvider);
    }

    private List<CurrencyRateEntity> getCurrencyRates(
        String baseCurrency, RateProviderEnum rateProvider, List<String> currencyNames) {
      return currencyRateRepository.findByCurrencyBaseCurrencyAndProvider(
          baseCurrency, currencyNames, rateProvider);
    }

    private Optional<CurrencyRateEntity> getCurrencyRate(
        String baseCurrency, String currencyName, RateProviderEnum rateProvider) {
      return currencyRateRepository.findByCurrencyBaseCurrencyAndProvider(
          baseCurrency, currencyName, rateProvider);
    }

    private void cacheRate(List<CurrencyDomainDTO> DTOs) {
      Duration ttl = Duration.ofSeconds(cacheTtlSeconds);
      DTOs.forEach(dto -> cacheRate(ttl, dto));
    }

    private void cacheRate(CurrencyDomainDTO dto) {
      Duration ttl = Duration.ofSeconds(cacheTtlSeconds);
      cacheRate(ttl, dto);
    }

    private void cacheRate(Duration ttl, CurrencyDomainDTO dto) {
        String key = RedisKeys.createCurrencyKey(dto.getBaseCurrency(), dto.getCurrency());
        redisService.cacheObject(key, ttl, dto);
    }
}
