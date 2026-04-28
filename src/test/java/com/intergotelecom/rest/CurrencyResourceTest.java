package com.intergotelecom.rest;

import static io.restassured.RestAssured.given;

import com.intergotelecom.config.BaseIntegrationTest;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.repository.CurrencyRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@RequiredArgsConstructor
class CurrencyResourceTest extends BaseIntegrationTest {
    private final
    CurrencyRepository currencyRepository;

    @BeforeEach
    void clean() {
        currencyRepository.deleteAll();
    }

    @Test
    void getAvailableCurrencies() {
        var euroCurrency = "EUR";
        var poundCurrency = "GBP";

        createCurrency(euroCurrency, true,true);
        createCurrency(poundCurrency, false, true);

        given()
            .accept(MediaType.APPLICATION_JSON)
        .when()
            .get("/api/v1/currency")
        .then()
            .statusCode(Status.OK.getStatusCode());
    }

    private void createCurrency(String currencyName, boolean isBase, boolean available) {
        var currencyEntity = new CurrencyEntity();
        currencyEntity.setCurrencyName(currencyName);
        currencyEntity.setBaseCurrency(isBase);
        currencyEntity.setAvailable(available);
        currencyEntity.setCreatedAt(LocalDateTime.now());

        currencyRepository.persist(currencyEntity);
    }
}
