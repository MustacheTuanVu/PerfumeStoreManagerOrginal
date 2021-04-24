package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.service.dto.MemberDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coderkiemcom.perfumestoremanager.domain.Member}.
 */
public interface MemberService {
    /**
     * Save a member.
     *
     * @param memberDTO the entity to save.
     * @return the persisted entity.
     */
    MemberDTO save(MemberDTO memberDTO);

    /**
     * Partially updates a member.
     *
     * @param memberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MemberDTO> partialUpdate(MemberDTO memberDTO);

    /**
     * Get all the members.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemberDTO> findAll(Pageable pageable);

    /**
     * Get the "id" member.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemberDTO> findOne(Long id);

    /**
     * Delete the "id" member.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
