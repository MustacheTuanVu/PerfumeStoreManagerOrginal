package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.ProductsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Products} and its DTO {@link ProductsDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoriesMapper.class })
public interface ProductsMapper extends EntityMapper<ProductsDTO, Products> {
    @Mapping(target = "categories", source = "categories", qualifiedByName = "id")
    ProductsDTO toDto(Products s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductsDTO toDtoId(Products products);
}
