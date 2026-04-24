package com.intergotelecom.repository;

import com.intergotelecom.model.CurrencyRateEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CurrencyRateRepository implements PanacheMongoRepository<CurrencyRateEntity> {
}
