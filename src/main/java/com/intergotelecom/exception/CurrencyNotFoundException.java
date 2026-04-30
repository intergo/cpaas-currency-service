package com.intergotelecom.exception;

import com.intergotelecom.enums.ErrorCodeEnum;
import jakarta.ws.rs.core.Response.Status;

public class CurrencyNotFoundException extends ServiceException {
    public CurrencyNotFoundException(ErrorCodeEnum errorCode, String currency) {
        super(errorCode, currency, Status.NOT_FOUND);
    }
}
