package com.intergotelecom.repository;

import com.intergotelecom.model.CurrencyEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CurrencyRepository implements PanacheRepository<CurrencyEntity> {
    public List<CurrencyEntity> findAvailable() {
        return list("available = ?1 and baseCurrency = ?2", true, false);
    }

    public List<CurrencyEntity> findAvailableByName(List<String> currencyNames) {
      return list("currencyName in ?1 and available = ?2 and baseCurrency = ?3",
          currencyNames, true, false
      );
    }

    public Optional<CurrencyEntity> findBaseCurrency() {
        return find("baseCurrency", true).firstResultOptional();
    }

    public Optional<CurrencyEntity> findByCurrencyName(String currencyName) {
        return find("currencyName", currencyName).firstResultOptional();
    }
}
