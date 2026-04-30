package com.intergotelecom.exception;

import com.intergotelecom.enums.ErrorCodeEnum;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;

@Getter
public abstract class ServiceException extends RuntimeException {
    private final Status status;
    private final ErrorCodeEnum errorCode;
    private final String message;

    protected ServiceException(ErrorCodeEnum errorCode, String error, Status status) {
        super(errorCode + error);
        this.status = status;
        this.errorCode = errorCode;
        this.message = errorCode.getValue() + error;
    }
}
