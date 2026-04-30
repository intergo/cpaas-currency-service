package com.intergotelecom.exception;

import com.intergotelecom.enums.ErrorCodeEnum;
import jakarta.ws.rs.core.Response.Status;

public class CustomRateNotFoundException extends ServiceException {
    public CustomRateNotFoundException(String currency) {
        super(ErrorCodeEnum.CUSTOM_RATE_NOT_FOUND, currency, Status.NOT_FOUND);
    }
}
