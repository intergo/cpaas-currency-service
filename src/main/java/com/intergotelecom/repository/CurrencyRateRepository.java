package com.intergotelecom.repository;

import com.intergotelecom.model.CurrencyRateEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CurrencyRateRepository implements PanacheRepository<CurrencyRateEntity> {
    public List<CurrencyRateEntity> findByCurrencyAndBaseCurrency(
        String baseCurrency, Set<String> currencyNames) {
      return findByCurrencyAndBaseCurrency(baseCurrency, List.copyOf(currencyNames));
    }

    public List<CurrencyRateEntity> findByCurrencyAndBaseCurrency(
        String baseCurrency, List<String> currencyNames) {
      return list("baseCurrency.currencyName = ?1 and currency.currencyName in ?2",
          baseCurrency, currencyNames);
    }

    public List<CurrencyRateEntity> findByBaseCurrency(String baseCurrencyName) {
      return list("baseCurrency.currencyName = ?1 and currency.available = ?2",
          baseCurrencyName, true);
    }
}
