package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.StoresDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.Stores}.
 */
public interface StoresService {
    /**
     * Save a stores.
     *
     * @param storesDTO the entity to save.
     * @return the persisted entity.
     */
    StoresDTO save(StoresDTO storesDTO);

    /**
     * Partially updates a stores.
     *
     * @param storesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StoresDTO> partialUpdate(StoresDTO storesDTO);

    /**
     * Get all the stores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StoresDTO> findAll(Pageable pageable);

    /**
     * Get the "id" stores.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StoresDTO> findOne(Long id);

    /**
     * Delete the "id" stores.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
