package com.intergotelecom.mapper;

import com.intergotelecom.model.CurrencyRateEntity;
import com.intergotelecom.rest.dto.CurrencyRateResponseDTO;
import com.intergotelecom.rest.dto.UpdateCurrencyRateRequestDTO;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.CDI, imports = {ObjectId.class, LocalDateTime.class})
public interface CurrencyRateMapper {
    @Mapping(target = "id", ignore = true)
    CurrencyRateResponseDTO toResponseDto(CurrencyRateEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currencyId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    CurrencyRateEntity toEntity(UpdateCurrencyRateRequestDTO dto);
}
