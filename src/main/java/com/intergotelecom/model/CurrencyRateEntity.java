package com.intergotelecom.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@MongoEntity(collection = "currency_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateEntity extends PanacheMongoEntity {
    @BsonProperty("currency_id")
    private ObjectId currencyId;

    private BigDecimal rate;

    @BsonProperty("created_at")
    private LocalDateTime createdAt;
}
