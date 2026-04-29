package com.intergotelecom.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RateProviderEnum {
  ECB("ecb"),
  CUSTOM("custom");

  private final String value;
}
