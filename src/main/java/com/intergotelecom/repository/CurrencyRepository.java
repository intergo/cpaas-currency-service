package com.intergotelecom.repository;

import com.intergotelecom.model.CurrencyEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CurrencyRepository implements PanacheMongoRepository<CurrencyEntity> {
}
