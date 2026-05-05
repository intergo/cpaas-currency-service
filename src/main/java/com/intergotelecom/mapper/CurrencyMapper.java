package com.intergotelecom.mapper;

import com.intergotelecom.dtos.currency_rates.CurrencyResponseDTO;
import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.rest.dto.CreateCurrencyRequestDTO;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.CDI, imports = {LocalDateTime.class})
public interface CurrencyMapper {
    @Mapping(target = "currencyName", source = "currencyName")
    @Mapping(target = "baseCurrency", source = "baseCurrency")
    @Mapping(target = "available", source = "available")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CurrencyResponseDTO toResponseDto(CurrencyEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CurrencyEntity toEntity(CreateCurrencyRequestDTO dto);
}
