package com.intergotelecom.rest;

import com.intergotelecom.rest.dto.CurrencyDto;
import com.intergotelecom.service.CurrencyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
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
        List<CurrencyDto> currencies = currencyService.getCurrencies();
        return Response.ok(currencies).build();
    }
}
