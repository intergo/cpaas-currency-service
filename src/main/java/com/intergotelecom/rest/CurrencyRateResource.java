package com.intergotelecom.rest;

import com.intergotelecom.rest.dto.CurrencyRatesResponseDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRateDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRatesRequestDTO;
import com.intergotelecom.service.CurrencyRateService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequestScoped
@Path("/api/v1/currency-rate")
@RequiredArgsConstructor
public class CurrencyRateResource {
    private final
    CurrencyRateService currencyRateService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrencyRates(
            @QueryParam("currencies") List<String> currencies) {
        CurrencyRatesResponseDTO responseDTO = currencyRateService
            .getCurrencyRatesResponse(currencies);

        return Response.ok(responseDTO).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setCurrencyRates(@Valid UpdateCurrencyRatesRequestDTO request) {
        List<UpdateCurrencyRateDTO> currencyRates = request.getCurrencyRates();

        CurrencyRatesResponseDTO responseDTO = currencyRateService
            .setCurrencyRates(currencyRates);

        return Response.ok(responseDTO).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/custom")
    public Response setCustomRates(@Valid UpdateCurrencyRateDTO requestDTO) {
        CurrencyRatesResponseDTO responseDTO = currencyRateService
            .setCustomRates(requestDTO);

      return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/custom")
    public Response deleteCustomRate(
        @QueryParam("currency") String currencyName) {
        CurrencyRatesResponseDTO responseDTO = currencyRateService
            .deleteCustomRate(currencyName);

        return Response.ok(responseDTO).build();
    }
}
