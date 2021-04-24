package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.StoresDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stores} and its DTO {@link StoresDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StoresMapper extends EntityMapper<StoresDTO, Stores> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoresDTO toDtoId(Stores stores);
}
