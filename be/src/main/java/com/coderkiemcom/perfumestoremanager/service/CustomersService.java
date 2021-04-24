package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.CustomersDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.Customers}.
 */
public interface CustomersService {
    /**
     * Save a customers.
     *
     * @param customersDTO the entity to save.
     * @return the persisted entity.
     */
    CustomersDTO save(CustomersDTO customersDTO);

    /**
     * Partially updates a customers.
     *
     * @param customersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomersDTO> partialUpdate(CustomersDTO customersDTO);

    /**
     * Get all the customers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" customers.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomersDTO> findOne(Long id);

    /**
     * Delete the "id" customers.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
