package com.intergotelecom.rest;

import static io.restassured.RestAssured.given;

import com.intergotelecom.config.BaseIntegrationTest;
import com.intergotelecom.factory.CurrencyDataFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@RequiredArgsConstructor
class CurrencyResourceTest extends BaseIntegrationTest {
    private final
    CurrencyDataFactory currencyDataFactory;

    @BeforeEach
    void setUp() {
        currencyDataFactory.cleanDatabase();
    }

    @Test
    void getAvailableCurrencies() {
        currencyDataFactory.createCurrency("EUR", true, true);
        currencyDataFactory.createCurrency("GBP", false, true);

        given()
            .accept(MediaType.APPLICATION_JSON)
        .when()
            .get("/api/v1/currency")
        .then()
            .statusCode(Status.OK.getStatusCode());
    }
}
