package com.intergotelecom.mapper;

import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.rest.dto.CreateCurrencyRequestDTO;
import com.intergotelecom.rest.dto.CurrencyResponseDTO;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.CDI, imports = {LocalDateTime.class})
public interface CurrencyMapper {
    CurrencyResponseDTO toResponseDto(CurrencyEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    CurrencyEntity toEntity(CreateCurrencyRequestDTO dto);
}
