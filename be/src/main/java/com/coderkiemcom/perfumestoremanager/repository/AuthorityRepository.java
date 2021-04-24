package com.coderkiemcom.perfumestoremanager.repository;

import com.coderkiemcom.perfumestoremanager.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
