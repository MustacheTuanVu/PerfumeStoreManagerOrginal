package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.DayWorksDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.DayWorks}.
 */
public interface DayWorksService {
    /**
     * Save a dayWorks.
     *
     * @param dayWorksDTO the entity to save.
     * @return the persisted entity.
     */
    DayWorksDTO save(DayWorksDTO dayWorksDTO);

    /**
     * Partially updates a dayWorks.
     *
     * @param dayWorksDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DayWorksDTO> partialUpdate(DayWorksDTO dayWorksDTO);

    /**
     * Get all the dayWorks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DayWorksDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dayWorks.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DayWorksDTO> findOne(Long id);

    /**
     * Delete the "id" dayWorks.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
