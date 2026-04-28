package com.intergotelecom.repository;

import com.intergotelecom.model.CustomCurrencyRateEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomCurrencyRateRepository
    implements PanacheRepository<CustomCurrencyRateEntity> {
}
