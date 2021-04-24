package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.domain.*; // for static metamodels
import com.coderkiemcom.perfumestoremanager.domain.Stores;
import com.coderkiemcom.perfumestoremanager.repository.StoresRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.StoresCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.StoresDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.StoresMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Stores} entities in the database.
 * The main input is a {@link StoresCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StoresDTO} or a {@link Page} of {@link StoresDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StoresQueryService extends QueryService<Stores> {

    private final Logger log = LoggerFactory.getLogger(StoresQueryService.class);

    private final StoresRepository storesRepository;

    private final StoresMapper storesMapper;

    public StoresQueryService(StoresRepository storesRepository, StoresMapper storesMapper) {
        this.storesRepository = storesRepository;
        this.storesMapper = storesMapper;
    }

    /**
     * Return a {@link List} of {@link StoresDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StoresDTO> findByCriteria(StoresCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stores> specification = createSpecification(criteria);
        return storesMapper.toDto(storesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StoresDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StoresDTO> findByCriteria(StoresCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stores> specification = createSpecification(criteria);
        return storesRepository.findAll(specification, page).map(storesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StoresCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stores> specification = createSpecification(criteria);
        return storesRepository.count(specification);
    }

    /**
     * Function to convert {@link StoresCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Stores> createSpecification(StoresCriteria criteria) {
        Specification<Stores> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Stores_.id));
            }
            if (criteria.getStoreID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoreID(), Stores_.storeID));
            }
            if (criteria.getStoreName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoreName(), Stores_.storeName));
            }
            if (criteria.getStorePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStorePhone(), Stores_.storePhone));
            }
            if (criteria.getStoreAdress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoreAdress(), Stores_.storeAdress));
            }
            if (criteria.getStoreRent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStoreRent(), Stores_.storeRent));
            }
            if (criteria.getMemberId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMemberId(), root -> root.join(Stores_.members, JoinType.LEFT).get(Member_.id))
                    );
            }
        }
        return specification;
    }
}
