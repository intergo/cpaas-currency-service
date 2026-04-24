package com.intergotelecom.mapper;

import com.intergotelecom.model.CurrencyEntity;
import com.intergotelecom.rest.dto.CurrencyDto;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.CDI, imports = ObjectId.class)
public interface CurrencyMapper {
    CurrencyDto toDto(CurrencyEntity entity);
}
