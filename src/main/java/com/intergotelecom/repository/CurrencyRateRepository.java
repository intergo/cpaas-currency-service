package com.intergotelecom.repository;

import com.intergotelecom.enums.RateProviderEnum;
import com.intergotelecom.model.CurrencyRateEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CurrencyRateRepository implements PanacheRepository<CurrencyRateEntity> {
    public List<CurrencyRateEntity> findByCurrencyBaseCurrencyAndProvider(
        String baseCurrency, List<String> currencyNames, RateProviderEnum rateProvider) {
      return list("baseCurrency.currencyName = ?1 "
              + "and currency.currencyName in ?2 "
              + "and rateProvider = ?3",
          baseCurrency, currencyNames, rateProvider);
    }

    public List<CurrencyRateEntity> findByBaseCurrencyAndCurrencyNames(
        String baseCurrency, List<String> currencyNames) {
      return list("baseCurrency.currencyName = ?1 "
              + "and currency.currencyName in ?2",
          baseCurrency, currencyNames);
    }

    public Optional<CurrencyRateEntity> findByCurrencyBaseCurrencyAndProvider(
        String baseCurrency, String currencyName, RateProviderEnum rateProvider) {
      return find("baseCurrency.currencyName = ?1 "
              + "and currency.currencyName = ?2 "
              + "and rateProvider = ?3",
          baseCurrency, currencyName, rateProvider)
          .firstResultOptional();
    }
}
