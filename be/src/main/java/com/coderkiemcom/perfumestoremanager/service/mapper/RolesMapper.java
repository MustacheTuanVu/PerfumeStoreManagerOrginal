package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.RolesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Roles} and its DTO {@link RolesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RolesMapper extends EntityMapper<RolesDTO, Roles> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RolesDTO toDtoId(Roles roles);
}
