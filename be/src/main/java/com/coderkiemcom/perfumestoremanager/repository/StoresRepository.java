package com.coderkiemcom.perfumestoremanager.repository;

import com.coderkiemcom.perfumestoremanager.domain.Stores;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Stores entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoresRepository extends JpaRepository<Stores, Long>, JpaSpecificationExecutor<Stores> {}
