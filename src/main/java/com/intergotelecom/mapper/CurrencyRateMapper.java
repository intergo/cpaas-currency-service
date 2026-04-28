package com.intergotelecom.mapper;

import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.model.CurrencyRateEntity;
import com.intergotelecom.rest.dto.CurrencyRateResponseDTO;
import com.intergotelecom.rest.dto.CurrencyRatesResponseDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRateDTO;
import com.intergotelecom.service.dto.CurrencyRedisDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.CDI)
public interface CurrencyRateMapper {
    @Mapping(target = "currencyName", source = "currency.currencyName")
    CurrencyRateResponseDTO toResponseDto(CurrencyRateEntity entity);

    List<CurrencyRateResponseDTO> toResponseDto(List<CurrencyRateEntity> entity);

    @Mapping(target = "baseCurrencyName", source = "baseCurrency")
    @Mapping(target = "currencyRates", source = "entity")
    CurrencyRatesResponseDTO toResponseDto(String baseCurrency, List<CurrencyRateEntity> entity);

    @Mapping(target = "baseCurrency", source = "baseCurrency")
    @Mapping(target = "rate", source = "dto.rate")
    @Mapping(target = "currency", source = "dto.currencyName")
    CurrencyRedisDTO toRedisDTO(
        String baseCurrency, UpdateCurrencyRateDTO dto);

    List<CurrencyRedisDTO> toRedisDTO(
        String baseCurrency, List<UpdateCurrencyRateDTO> dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rate", source = "dto.rate")
    @Mapping(target = "currency", source = "currencyEntity")
    @Mapping(target = "baseCurrency", source = "baseCurrencyEntity")
    CurrencyRateEntity toEntity(
        UpdateCurrencyRateDTO dto,
        CurrencyEntity currencyEntity,
        CurrencyEntity baseCurrencyEntity);
}
