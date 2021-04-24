package com.coderkiemcom.perfumestoremanager.repository;

import com.coderkiemcom.perfumestoremanager.domain.DayWorks;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DayWorks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DayWorksRepository extends JpaRepository<DayWorks, Long>, JpaSpecificationExecutor<DayWorks> {}
