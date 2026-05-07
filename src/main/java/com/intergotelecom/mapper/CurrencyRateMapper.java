package com.intergotelecom.mapper;

import com.intergotelecom.dtos.currency_rates.CurrencyCacheDTO;
import com.intergotelecom.dtos.currency_rates.CurrencyRateResponseDTO;
import com.intergotelecom.dtos.currency_rates.CurrencyRatesResponseDTO;
import com.intergotelecom.dtos.currency_rates.UpdateCurrencyRateDTO;
import com.intergotelecom.enums.RateProviderEnum;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.model.CurrencyRateEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.CDI)
public interface CurrencyRateMapper {
    @Mapping(target = "currencyName", source = "currency")
    @Mapping(target = "rate", source = "rate")
    CurrencyRateResponseDTO toResponseDto(CurrencyCacheDTO domainDTO);

    List<CurrencyRateResponseDTO> toResponseDto(List<CurrencyCacheDTO> dtos);

    @Mapping(target = "baseCurrencyName", source = "baseCurrency")
    @Mapping(target = "currencyRates", source = "dtos")
    CurrencyRatesResponseDTO toResponseDto(String baseCurrency, List<CurrencyCacheDTO> dtos);

    @Mapping(target = "baseCurrency", source = "baseCurrency.currencyName")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "currency", source = "currency.currencyName")
    CurrencyCacheDTO toDomainDTO(CurrencyRateEntity entity);

    List<CurrencyCacheDTO> toDomainDTO(List<CurrencyRateEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rate", source = "dto.rate")
    @Mapping(target = "rateProvider", source = "rateProvider")
    @Mapping(target = "currency", source = "currencyEntity")
    @Mapping(target = "baseCurrency", source = "baseCurrencyEntity")
    CurrencyRateEntity toEntity(
        UpdateCurrencyRateDTO dto,
        RateProviderEnum rateProvider,
        CurrencyEntity currencyEntity,
        CurrencyEntity baseCurrencyEntity);
}
