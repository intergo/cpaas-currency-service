package com.intergotelecom.repository;

import com.intergotelecom.enums.RateProviderEnum;
import com.intergotelecom.model.CurrencyRateEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CurrencyRateRepository implements PanacheRepository<CurrencyRateEntity> {
    public List<CurrencyRateEntity> findByCurrencyBaseCurrencyAndProvider(
        String baseCurrency, Set<String> currencyNames, RateProviderEnum rateProvider) {
      return findByCurrencyBaseCurrencyAndProvider(
          baseCurrency, List.copyOf(currencyNames), rateProvider);
    }

    public List<CurrencyRateEntity> findByCurrencyBaseCurrencyAndProvider(
        String baseCurrency, List<String> currencyNames, RateProviderEnum rateProvider) {
      return list("baseCurrency.currencyName = ?1 "
              + "and currency.currencyName in ?2 "
              + "and rateProvider = ?3",
          baseCurrency, currencyNames, rateProvider);
    }

    public List<CurrencyRateEntity> findByBaseCurrency(String baseCurrencyName) {
      return list("baseCurrency.currencyName = ?1 and currency.available = ?2",
          baseCurrencyName, true);
    }
}
