package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.RolesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.Roles}.
 */
public interface RolesService {
    /**
     * Save a roles.
     *
     * @param rolesDTO the entity to save.
     * @return the persisted entity.
     */
    RolesDTO save(RolesDTO rolesDTO);

    /**
     * Partially updates a roles.
     *
     * @param rolesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RolesDTO> partialUpdate(RolesDTO rolesDTO);

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RolesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" roles.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RolesDTO> findOne(Long id);

    /**
     * Delete the "id" roles.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
