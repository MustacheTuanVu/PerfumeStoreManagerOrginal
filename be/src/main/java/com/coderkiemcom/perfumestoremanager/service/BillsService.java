package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.BillsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.Bills}.
 */
public interface BillsService {
    /**
     * Save a bills.
     *
     * @param billsDTO the entity to save.
     * @return the persisted entity.
     */
    BillsDTO save(BillsDTO billsDTO);

    /**
     * Partially updates a bills.
     *
     * @param billsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillsDTO> partialUpdate(BillsDTO billsDTO);

    /**
     * Get all the bills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bills.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillsDTO> findOne(Long id);

    /**
     * Delete the "id" bills.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
