package com.intergotelecom.repository;

import com.intergotelecom.model.CustomCurrencyRateEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomCurrencyRateRepository
    implements PanacheMongoRepository<CustomCurrencyRateEntity> {
}
