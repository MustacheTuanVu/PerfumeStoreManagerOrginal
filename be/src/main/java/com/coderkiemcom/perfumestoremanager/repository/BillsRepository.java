package com.coderkiemcom.perfumestoremanager.repository;

import com.coderkiemcom.perfumestoremanager.domain.Bills;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillsRepository extends JpaRepository<Bills, Long>, JpaSpecificationExecutor<Bills> {}
