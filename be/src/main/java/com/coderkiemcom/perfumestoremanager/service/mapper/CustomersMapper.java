package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.CustomersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customers} and its DTO {@link CustomersDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomersMapper extends EntityMapper<CustomersDTO, Customers> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomersDTO toDtoId(Customers customers);
}
