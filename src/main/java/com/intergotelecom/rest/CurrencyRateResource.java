package com.intergotelecom.rest;

import com.intergotelecom.rest.dto.CurrencyRateResponseDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRateRequestDTO;
import com.intergotelecom.service.CurrencyRateService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Path("/api/v1/currency-rate")
@RequiredArgsConstructor
public class CurrencyRateResource {
    private final
    CurrencyRateService currencyRateService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setCurrencyRate(@Valid UpdateCurrencyRateRequestDTO request) {
        CurrencyRateResponseDTO created = currencyRateService.setCurrencyRate(request);
        return Response.status(Status.CREATED).entity(created).build();
    }
}
