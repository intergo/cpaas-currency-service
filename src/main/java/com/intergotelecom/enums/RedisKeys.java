package com.intergotelecom.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeys {
  CURRENCY_CODE("currency-code:");

  private final String value;

  public static String createKey(RedisKeys keyPrefix, String keyId) {
    return keyPrefix.getValue() + keyId;
  }

  public static String createCurrencyKey(String baseCurrency, String currency) {
    return createKey(CURRENCY_CODE, baseCurrency + ":" + currency);
  }
}
