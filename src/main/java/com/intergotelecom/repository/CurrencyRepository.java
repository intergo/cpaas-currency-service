package com.intergotelecom.repository;

import com.intergotelecom.model.CurrencyEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CurrencyRepository implements PanacheMongoRepository<CurrencyEntity> {
    public List<CurrencyEntity> findAvailable() {
        return list("available", true);
    }
}
