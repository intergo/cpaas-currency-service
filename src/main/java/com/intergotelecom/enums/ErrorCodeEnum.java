package com.intergotelecom.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeEnum {
    CURRENCY_NOT_FOUND("Currency not found: "),
    BASE_CURRENCY_NOT_FOUND("Base currency not found: "),
    CUSTOM_RATE_NOT_FOUND("Custom rate not found for currency: ");

    private final String value;
}
