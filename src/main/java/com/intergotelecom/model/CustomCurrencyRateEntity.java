package com.intergotelecom.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@MongoEntity(collection = "custom_currency_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomCurrencyRateEntity extends PanacheMongoEntity {
    private ObjectId currencyId;

    private BigDecimal rate;

    private String userId;

    private LocalDateTime createdAt;
}
