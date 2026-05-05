package com.intergotelecom.rest;

import com.intergotelecom.dtos.currency_rates.CurrencyListResponseDTO;
import com.intergotelecom.dtos.currency_rates.CurrencyResponseDTO;
import com.intergotelecom.rest.dto.CreateCurrencyRequestDTO;
import com.intergotelecom.service.CurrencyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Path("/api/v1/currency")
@RequiredArgsConstructor
public class CurrencyResource {
    private final
    CurrencyService currencyService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrencies() {
        CurrencyListResponseDTO currencies = currencyService.getCurrencies();
        return Response.ok(currencies).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCurrency(@Valid CreateCurrencyRequestDTO createCurrencyRequestDTO) {
        CurrencyResponseDTO created = currencyService.createCurrency(createCurrencyRequestDTO);

        return Response.status(Status.CREATED)
            .entity(created)
            .build();
    }
}
