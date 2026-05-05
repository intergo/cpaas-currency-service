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
    CurrencyResponseDTO toResponseDto(CurrencyEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CurrencyEntity toEntity(CreateCurrencyRequestDTO dto);
}
