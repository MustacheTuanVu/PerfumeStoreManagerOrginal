package com.coderkiemcom.perfumestoremanager.service.mapper;

import com.coderkiemcom.perfumestoremanager.domain.*;
import com.coderkiemcom.perfumestoremanager.service.dto.DayWorksDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DayWorks} and its DTO {@link DayWorksDTO}.
 */
@Mapper(componentModel = "spring", uses = { MemberMapper.class })
public interface DayWorksMapper extends EntityMapper<DayWorksDTO, DayWorks> {
    @Mapping(target = "member", source = "member", qualifiedByName = "id")
    DayWorksDTO toDto(DayWorks s);
}
