package com.intergotelecom.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.Instant;

@MongoEntity(collection = "currency_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateEntity extends PanacheMongoEntity {
    private ObjectId currencyId;

    private BigDecimal rate;

    private LocalDateTime createdAt;
}
