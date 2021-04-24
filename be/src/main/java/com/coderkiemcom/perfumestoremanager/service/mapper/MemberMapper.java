package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.MemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring", uses = { StoresMapper.class, RolesMapper.class })
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "stores", source = "stores", qualifiedByName = "id")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "id")
    MemberDTO toDto(Member s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoId(Member member);
}
