package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.CategoriesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.Categories}.
 */
public interface CategoriesService {
    /**
     * Save a categories.
     *
     * @param categoriesDTO the entity to save.
     * @return the persisted entity.
     */
    CategoriesDTO save(CategoriesDTO categoriesDTO);

    /**
     * Partially updates a categories.
     *
     * @param categoriesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoriesDTO> partialUpdate(CategoriesDTO categoriesDTO);

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CategoriesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" categories.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoriesDTO> findOne(Long id);

    /**
     * Delete the "id" categories.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
