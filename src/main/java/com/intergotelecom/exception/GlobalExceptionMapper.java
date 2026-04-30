package com.intergotelecom.exception;

import com.intergotelecom.rest.dto.ErrorResponseDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<ServiceException> {
    @Override
    public Response toResponse(ServiceException exception) {
        int status = exception.getStatus().getStatusCode();

        var errorResponse = ErrorResponseDTO.builder()
            .status(status)
            .errorCode(exception.getErrorCode())
            .message(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build();

        return Response.status(status)
            .entity(errorResponse)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
