package com.intergotelecom.rest;

import static com.intergotelecom.enums.ErrorCodeEnum.BASE_CURRENCY_NOT_FOUND;
import static com.intergotelecom.enums.ErrorCodeEnum.CURRENCY_NOT_FOUND;
import static com.intergotelecom.enums.ErrorCodeEnum.CUSTOM_RATE_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.intergotelecom.config.BaseIntegrationTest;
import com.intergotelecom.enums.RateProviderEnum;
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
    void setCurrencyRates_updatesExistingRates() {
        var eur = currencyDataFactory.createCurrency("EUR", true, true);
        var usd = currencyDataFactory.createCurrency("USD", false, true);

        var ecb = RateProviderEnum.ECB;

        currencyDataFactory.createCurrency("GBP", false, true);
        currencyDataFactory.createCurrencyRate(eur, usd, ecb, new BigDecimal("1.08"));

        var usdRate = UpdateCurrencyRateDTO.builder()
            .currencyName("USD")
            .rate(new BigDecimal("1.15"))
            .build();

        var gbpRate = UpdateCurrencyRateDTO.builder()
            .currencyName("GBP")
            .rate(new BigDecimal("0.90"))
            .build();

        var request = UpdateCurrencyRatesRequestDTO.builder()
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
    void getCurrencyRates_returnsRatesForBaseCurrency() {
      // create currencies
      var eur = currencyDataFactory.createCurrency("EUR", true, true);
      var usd = currencyDataFactory.createCurrency("USD", false, true);
      var gbp = currencyDataFactory.createCurrency("GBP", false, true);

      var ecb = RateProviderEnum.ECB;

      // create rates
      currencyDataFactory.createCurrencyRate(eur, usd, ecb, new BigDecimal("1.08"));
      currencyDataFactory.createCurrencyRate(eur, gbp, ecb, new BigDecimal("0.86"));

      given()
          .accept(MediaType.APPLICATION_JSON)
      .when()
          .queryParam("currencies", "USD")
          .queryParam("currencies", "GBP")
          .get("/api/v1/currency-rate")
      .then()
          .statusCode(Status.OK.getStatusCode())
          .body("base_currency_name", is("EUR"))
          .body("currency_rates", hasSize(2));
    }

    @Test
    void getCurrencyRates_returnsOnlyRequestedCurrencies() {
        var eur = currencyDataFactory
            .createCurrency("EUR", true, true);

        var usd = currencyDataFactory
            .createCurrency("USD", false, true);

        var gbp = currencyDataFactory
            .createCurrency("GBP", false, true);

        var jpy = currencyDataFactory
            .createCurrency("JPY", false, true);

        var ecb = RateProviderEnum.ECB;

        currencyDataFactory.createCurrencyRate(eur, usd, ecb, new BigDecimal("1.08"));
        currencyDataFactory.createCurrencyRate(eur, gbp, ecb, new BigDecimal("0.86"));
        currencyDataFactory.createCurrencyRate(eur, jpy, ecb, new BigDecimal("160.00"));

        given()
            .queryParam("base_currency", "EUR")
            .queryParam("currencies", "USD")
            .queryParam("currencies", "GBP")
        .when()
            .get("/api/v1/currency-rate")
        .then()
            .statusCode(Status.OK.getStatusCode())
            .body("base_currency_name", is("EUR"))
            .body("currency_rates", hasSize(2));
    }

    @Test
    void getCurrencyRates_returnsEmptyList_whenNoRatesExist() {
      currencyDataFactory.createCurrency("EUR", true, true);

      given()
          .queryParam("base_currency", "EUR")
      .when()
          .get("/api/v1/currency-rate")
      .then()
          .statusCode(Status.OK.getStatusCode())
          .body("base_currency_name", is("EUR"))
          .body("currency_rates", hasSize(0));
    }

    @Test
    void setCustomRates_createsNewCustomRates() {
      currencyDataFactory.createCurrency("EUR", true, true);
      currencyDataFactory.createCurrency("USD", false, true);

      var customRateRequest = UpdateCurrencyRateDTO.builder()
          .currencyName("USD")
          .rate(new BigDecimal("1.20"))
          .build();

      given()
          .basePath("/api/v1/currency-rate")
          .contentType(MediaType.APPLICATION_JSON)
          .body(customRateRequest)
      .when()
          .post("/custom")
      .then()
          .statusCode(Status.OK.getStatusCode())
          .body("base_currency_name", is("EUR"))
          .body("currency_rates", hasSize(1))
          .body("currency_rates[0].rate", is(customRateRequest.getRate().floatValue()));
    }

    @Test
    void setCustomRates_updatesExistingCustomRates() {
      var eur = currencyDataFactory.createCurrency("EUR", true, true);
      var usd = currencyDataFactory.createCurrency("USD", false, true);

      var providerEnum = RateProviderEnum.CUSTOM;

      // create existing CUSTOM rate
      currencyDataFactory.createCurrencyRate(
          eur, usd, providerEnum, new BigDecimal("1.20"));

      var customRateRequest = UpdateCurrencyRateDTO.builder()
          .currencyName("USD")
          .rate(new BigDecimal("1.25"))
          .build();

      given()
          .basePath("/api/v1/currency-rate")
          .contentType(MediaType.APPLICATION_JSON)
          .body(customRateRequest)
      .when()
          .post("/custom")
      .then()
          .statusCode(Status.OK.getStatusCode())
          .body("base_currency_name", is("EUR"))
          .body("currency_rates", hasSize(1))
          .body("currency_rates[0].rate", is(customRateRequest.getRate().floatValue()));
    }

    @Test
    void setCustomRates_overridesEcbRateInCache() {
        var eur = currencyDataFactory
            .createCurrency("EUR", true, true);

        var usd = currencyDataFactory
            .createCurrency("USD", false, true);

        // create ECB rate
        currencyDataFactory.createCurrencyRate(
            eur, usd, RateProviderEnum.ECB, BigDecimal.valueOf(1.08));

        // set custom rate via endpoint
        var customUsdRateRequest = UpdateCurrencyRateDTO.builder()
            .currencyName("USD")
            .rate(new BigDecimal("1.25"))
            .build();

        given()
            .basePath("/api/v1/currency-rate")
            .contentType(MediaType.APPLICATION_JSON)
            .body(customUsdRateRequest)
        .when()
            .post("/custom")
        .then()
            .statusCode(Status.OK.getStatusCode());

        // GET should return the custom rate
        given()
            .queryParam("currencies", "USD")
        .when()
            .get("/api/v1/currency-rate")
        .then()
            .statusCode(Status.OK.getStatusCode())
            .body("currency_rates", hasSize(1))
            .body("currency_rates[0].rate",
                is(customUsdRateRequest.getRate().floatValue()));
    }

    @Test
    void setCustomRate_returnsNotFound_whenCurrencyDoesNotExist() {
      currencyDataFactory.createCurrency("EUR", true, true);

      var customRateRequest = UpdateCurrencyRateDTO.builder()
          .currencyName("XYZ")
          .rate(new BigDecimal("1.50"))
          .build();

      given()
          .basePath("/api/v1/currency-rate")
          .contentType(MediaType.APPLICATION_JSON)
          .body(customRateRequest)
      .when()
          .post("/custom")
      .then()
          .statusCode(Status.NOT_FOUND.getStatusCode())
          .body("status", is(Status.NOT_FOUND.getStatusCode()))
          .body("error_code", is(CURRENCY_NOT_FOUND.name()))
          .body("message", containsString(CURRENCY_NOT_FOUND.getValue()))
          .body("timestamp", notNullValue());
    }

    @Test
    void setCustomRate_returnsNotFound_whenBaseCurrencyDoesNotExist() {
      currencyDataFactory.createCurrency("USD", false, true);

      var customRateRequest = UpdateCurrencyRateDTO.builder()
          .currencyName("USD")
          .rate(new BigDecimal("1.50"))
          .build();

      given()
          .basePath("/api/v1/currency-rate")
          .contentType(MediaType.APPLICATION_JSON)
          .body(customRateRequest)
      .when()
          .post("/custom")
      .then()
          .statusCode(Status.NOT_FOUND.getStatusCode())
          .body("status", is(Status.NOT_FOUND.getStatusCode()))
          .body("error_code", is(BASE_CURRENCY_NOT_FOUND.name()))
          .body("message", containsString(BASE_CURRENCY_NOT_FOUND.getValue()))
          .body("timestamp", notNullValue());
    }

    @Test
    void deleteCustomRate_removesBackofficeRate() {
        var eur = currencyDataFactory
            .createCurrency("EUR", true, true);

        var usd = currencyDataFactory
            .createCurrency("USD", false, true);

        var usdEcbRate = BigDecimal.valueOf(1.08);
        var usdCustomRate = BigDecimal.valueOf(1.20);

        // create ECB and CUSTOM rates
        currencyDataFactory.createCurrencyRate(
            eur, usd, RateProviderEnum.ECB, usdEcbRate);

        currencyDataFactory.createCurrencyRate(
            eur, usd, RateProviderEnum.CUSTOM, usdCustomRate);

        given()
            .basePath("/api/v1/currency-rate")
            .contentType(MediaType.APPLICATION_JSON)
            .queryParam("currency", "USD")
        .when()
            .delete("/custom")
        .then()
            .statusCode(Status.OK.getStatusCode())
            .body("base_currency_name", is("EUR"))
            .body("currency_rates", hasSize(1))
            .body("currency_rates[0].rate", is(usdCustomRate.floatValue()));
    }

    @Test
    void deleteCustomRate_returnsNotFound_whenNoCustomRateExists() {
        currencyDataFactory.createCurrency("EUR", true, true);
        currencyDataFactory.createCurrency("USD", false, true);

        given()
            .basePath("/api/v1/currency-rate")
            .queryParam("currency", "USD")
        .when()
            .delete("/custom")
        .then()
            .statusCode(Status.NOT_FOUND.getStatusCode())
            .body("status", is(Status.NOT_FOUND.getStatusCode()))
            .body("error_code", is(CUSTOM_RATE_NOT_FOUND.name()))
            .body("message", containsString(CUSTOM_RATE_NOT_FOUND.getValue()))
            .body("timestamp", notNullValue());
    }
}
