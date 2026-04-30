package com.intergotelecom.exception;

import com.intergotelecom.enums.ErrorCodeEnum;
import jakarta.ws.rs.core.Response.Status;

public class CurrencyAlreadyExistsException extends ServiceException {
    public CurrencyAlreadyExistsException(String currency) {
        super(ErrorCodeEnum.CURRENCY_ALREADY_EXISTS, currency, Status.CONFLICT);
    }
}
