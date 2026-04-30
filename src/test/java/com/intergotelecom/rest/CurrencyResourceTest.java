package com.intergotelecom.rest;

import static com.intergotelecom.enums.ErrorCodeEnum.CURRENCY_ALREADY_EXISTS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.intergotelecom.config.BaseIntegrationTest;
import com.intergotelecom.factory.CurrencyDataFactory;
import com.intergotelecom.rest.dto.CreateCurrencyRequestDTO;
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
    void createCurrency_returnsCreatedCurrency() {
        var request = CreateCurrencyRequestDTO.builder()
            .currencyName("USD")
            .baseCurrency(false)
            .available(true)
            .build();

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .post("/api/v1/currency")
        .then()
            .statusCode(Status.CREATED.getStatusCode())
            .body("currency_name", is("USD"));
    }

    @Test
    void createCurrency_returnsBadRequest_whenCurrencyNameBlank() {
        var request = CreateCurrencyRequestDTO.builder()
            .currencyName("")
            .available(true)
            .build();

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .post("/api/v1/currency")
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void createCurrency_returnsConflict_whenCurrencyAlreadyExists() {
        // create a currency entity
        currencyDataFactory.createCurrency("USD", false, true);

        // create the same entity using api
        var request = CreateCurrencyRequestDTO.builder()
            .currencyName("USD")
            .baseCurrency(false)
            .available(true)
            .build();

        // assert an error is thrown
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .post("/api/v1/currency")
        .then()
            .statusCode(Status.CONFLICT.getStatusCode())
            .body("status", is(Status.CONFLICT.getStatusCode()))
            .body("error_code", is(CURRENCY_ALREADY_EXISTS.name()))
            .body("message", containsString(CURRENCY_ALREADY_EXISTS.getValue()))
            .body("timestamp", notNullValue());
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
