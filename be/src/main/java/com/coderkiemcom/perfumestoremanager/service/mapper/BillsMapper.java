package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.BillsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bills} and its DTO {@link BillsDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomersMapper.class, MemberMapper.class })
public interface BillsMapper extends EntityMapper<BillsDTO, Bills> {
    @Mapping(target = "customers", source = "customers", qualifiedByName = "id")
    @Mapping(target = "member", source = "member", qualifiedByName = "id")
    BillsDTO toDto(Bills s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillsDTO toDtoId(Bills bills);
}
