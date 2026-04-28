package com.intergotelecom.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.intergotelecom.config.BaseIntegrationTest;
import com.intergotelecom.factory.CurrencyDataFactory;
import com.intergotelecom.rest.dto.UpdateCurrencyRateDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRatesRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@RequiredArgsConstructor
class CurrencyRateResourceTest extends BaseIntegrationTest {
    private final
    CurrencyDataFactory currencyDataFactory;

    @BeforeEach
    void setUp() {
        currencyDataFactory.cleanDatabase();
    }

    @Test
    void setCurrencyRates_createsNewRates() {
        // create new currencies with EUR as base
        currencyDataFactory.createCurrency("EUR", true, true);
        currencyDataFactory.createCurrency("USD", false, true);
        currencyDataFactory.createCurrency("GBP", false, true);

        var usdRate = UpdateCurrencyRateDTO.builder()
            .currencyName("USD")
            .rate(new BigDecimal("1.08"))
            .build();

        var gbpRate = UpdateCurrencyRateDTO.builder()
            .currencyName("GBP")
            .rate(new BigDecimal("0.86"))
            .build();

        // update usd and gbp rates
        var request = UpdateCurrencyRatesRequestDTO.builder()
            .baseCurrencyName("EUR")
            .currencyRates(List.of(usdRate, gbpRate))
            .build();

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .post("/api/v1/currency-rate")
        .then()
            .log().all()
            .statusCode(Status.OK.getStatusCode())
            .body("base_currency_name", is("EUR"))
            .body("currency_rates", hasSize(request.getCurrencyRates().size()));
    }

    @Test
    void setCurrencyRates_updatesExistingRates() {
        var eur = currencyDataFactory.createCurrency("EUR", true, true);
        var usd = currencyDataFactory.createCurrency("USD", false, true);
        currencyDataFactory.createCurrency("GBP", false, true);
        currencyDataFactory.createCurrencyRate(usd, eur, new BigDecimal("1.08"));

        var usdRate = UpdateCurrencyRateDTO.builder()
            .currencyName("USD")
            .rate(new BigDecimal("1.15"))
            .build();

        var gbpRate = UpdateCurrencyRateDTO.builder()
            .currencyName("GBP")
            .rate(new BigDecimal("0.90"))
            .build();

        var request = UpdateCurrencyRatesRequestDTO.builder()
            .baseCurrencyName("EUR")
            .currencyRates(List.of(usdRate, gbpRate))
            .build();

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .post("/api/v1/currency-rate")
        .then()
            .statusCode(Status.OK.getStatusCode())
            .body("base_currency_name", is("EUR"))
            .body("currency_rates", hasSize(request.getCurrencyRates().size()));
    }

    @Test
    void setCurrencyRates_returnsBadRequest_whenRatesListEmpty() {
        var request = UpdateCurrencyRatesRequestDTO.builder()
            .baseCurrencyName("EUR")
            .currencyRates(List.of())
            .build();

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .post("/api/v1/currency-rate")
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void setCurrencyRates_returnsBadRequest_whenBaseCurrencyBlank() {
        var usdRate = UpdateCurrencyRateDTO.builder()
            .currencyName("USD")
            .rate(new BigDecimal("1.08"))
            .build();

        var request = UpdateCurrencyRatesRequestDTO.builder()
            .baseCurrencyName("")
            .currencyRates(List.of(usdRate))
            .build();

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .post("/api/v1/currency-rate")
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }
}
