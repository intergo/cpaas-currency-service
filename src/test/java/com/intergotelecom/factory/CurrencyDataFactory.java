package com.intergotelecom.factory;

import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.model.CurrencyRateEntity;
import com.intergotelecom.repository.CurrencyRateRepository;
import com.intergotelecom.repository.CurrencyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CurrencyDataFactory {
    private final
    CurrencyRepository currencyRepository;

    private final
    CurrencyRateRepository currencyRateRepository;

    @Transactional
    public void cleanDatabase() {
        currencyRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Transactional
    public CurrencyEntity createCurrency(String currencyName, boolean isBase, boolean available) {
        var entity = CurrencyEntity.builder()
            .currencyName(currencyName)
            .baseCurrency(isBase)
            .available(available)
            .build();

        currencyRepository.persist(entity);
        return entity;
    }

    @Transactional
    public CurrencyRateEntity createCurrencyRate(
            CurrencyEntity currency, CurrencyEntity baseCurrency, BigDecimal rate) {
        var entity = CurrencyRateEntity.builder()
            .currency(currency)
            .baseCurrency(baseCurrency)
            .rate(rate)
            .build();

        currencyRateRepository.persist(entity);
        return entity;
    }
}
