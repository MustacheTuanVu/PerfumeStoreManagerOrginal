package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.ProductsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.Products}.
 */
public interface ProductsService {
    /**
     * Save a products.
     *
     * @param productsDTO the entity to save.
     * @return the persisted entity.
     */
    ProductsDTO save(ProductsDTO productsDTO);

    /**
     * Partially updates a products.
     *
     * @param productsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductsDTO> partialUpdate(ProductsDTO productsDTO);

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" products.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductsDTO> findOne(Long id);

    /**
     * Delete the "id" products.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
