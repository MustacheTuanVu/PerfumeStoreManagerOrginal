package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.OrderDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderDetails} and its DTO {@link OrderDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { BillsMapper.class, ProductsMapper.class })
public interface OrderDetailsMapper extends EntityMapper<OrderDetailsDTO, OrderDetails> {
    @Mapping(target = "bills", source = "bills", qualifiedByName = "id")
    @Mapping(target = "products", source = "products", qualifiedByName = "id")
    OrderDetailsDTO toDto(OrderDetails s);
}
