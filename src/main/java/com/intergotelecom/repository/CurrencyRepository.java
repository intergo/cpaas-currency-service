package com.intergotelecom.repository;

import com.intergotelecom.model.CurrencyEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CurrencyRepository implements PanacheMongoRepository<CurrencyEntity> {
    public List<CurrencyEntity> findAvailable() {
        return list("available = ?1 and baseCurrency = ?2", true, false);
    }

    public Optional<CurrencyEntity> findBaseCurrency() {
        return find("baseCurrency", true).firstResultOptional();
    }
}
