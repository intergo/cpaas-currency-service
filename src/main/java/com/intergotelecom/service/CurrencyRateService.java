package com.intergotelecom.service;

import com.intergotelecom.enums.RedisKeys;
import com.intergotelecom.mapper.CurrencyRateMapper;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.model.CurrencyRateEntity;
import com.intergotelecom.repository.CurrencyRateRepository;
import com.intergotelecom.rest.dto.CurrencyRatesResponseDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRateDTO;
import com.intergotelecom.service.dto.CurrencyRedisDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
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
    RedisService<CurrencyRedisDTO> redisService;

    @ConfigProperty(name = "currency.rate.cache.ttl", defaultValue = "3600")
    private long cacheTtlSeconds;

    public CurrencyRatesResponseDTO setCurrencyRates(
            String baseCurrencyName, List<UpdateCurrencyRateDTO> currencyRatesDTOs) {
        // create a map with rate DTOs
        Map<String, UpdateCurrencyRateDTO> currencyRatesMap = currencyRatesDTOs.stream()
            .collect(Collectors.toMap(
                UpdateCurrencyRateDTO::getCurrencyName,
                Function.identity()));

        Set<String> currenciesToUpdate = currencyRatesMap.keySet();

        // fetch existing rate entities
        List<CurrencyRateEntity> rateEntities = getCurrencyRates(
            baseCurrencyName, currenciesToUpdate);

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
            return currencyRateMapper.toResponseDto(baseCurrencyName, rateEntities);
        }

        // fetch base currency
        CurrencyEntity baseCurrencyEntity = currencyService.getBaseCurrencyOptional()
            .orElseThrow(() -> new NotFoundException("Base currency not found: " + baseCurrencyName));

        // create new rate entities
        List<CurrencyRateEntity> newRateEntities = missingCurrencies.stream()
            .map(currencyEntity -> {
              UpdateCurrencyRateDTO rateDTO = currencyRatesMap.get(currencyEntity.getCurrencyName());
              return currencyRateMapper.toEntity(rateDTO, currencyEntity, baseCurrencyEntity);
          })
          .toList();

        // persist new entities
        currencyRateRepository.persist(newRateEntities);

        // create response and return
        rateEntities.addAll(newRateEntities);

        // cache currency rates to redis
        List<CurrencyRedisDTO> redisDTOs = currencyRateMapper.toRedisDTO(baseCurrencyName, currencyRatesDTOs);
        cacheRates(baseCurrencyName, redisDTOs);

        return currencyRateMapper.toResponseDto(baseCurrencyName, rateEntities);
    }

    public CurrencyRatesResponseDTO getCurrencyRatesResponse(String baseCurrencyName, List<String> currencyNames) {
      List<CurrencyRateEntity> rates = currencyRateRepository
          .findByCurrencyAndBaseCurrency(baseCurrencyName, currencyNames);

      return currencyRateMapper.toResponseDto(baseCurrencyName, rates);
    }

    private List<CurrencyRateEntity> getCurrencyRates(String baseCurrency, Set<String> currencyNames) {
      return currencyRateRepository.findByCurrencyAndBaseCurrency(
          baseCurrency, currencyNames);
    }

    private void cacheRates(String baseCurrencyName, List<CurrencyRedisDTO> dtos) {
      Duration ttl = Duration.ofSeconds(cacheTtlSeconds);

      dtos.forEach(dto -> {
        String key = RedisKeys.createCurrencyKey(baseCurrencyName, dto.getCurrency());
        redisService.cacheObject(key, ttl, dto);
      });
    }
}
