package com.intergotelecom.repository;

import com.intergotelecom.model.CurrencyRateEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CurrencyRateRepository implements PanacheRepository<CurrencyRateEntity> {
}
