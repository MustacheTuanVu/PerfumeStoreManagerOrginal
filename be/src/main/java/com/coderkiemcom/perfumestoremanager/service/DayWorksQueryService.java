package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.domain.*; // for static metamodels
import com.coderkiemcom.perfumestoremanager.domain.DayWorks;
import com.coderkiemcom.perfumestoremanager.repository.DayWorksRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.DayWorksCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.DayWorksDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.DayWorksMapper;
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
 * Service for executing complex queries for {@link DayWorks} entities in the database.
 * The main input is a {@link DayWorksCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DayWorksDTO} or a {@link Page} of {@link DayWorksDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DayWorksQueryService extends QueryService<DayWorks> {

    private final Logger log = LoggerFactory.getLogger(DayWorksQueryService.class);

    private final DayWorksRepository dayWorksRepository;

    private final DayWorksMapper dayWorksMapper;

    public DayWorksQueryService(DayWorksRepository dayWorksRepository, DayWorksMapper dayWorksMapper) {
        this.dayWorksRepository = dayWorksRepository;
        this.dayWorksMapper = dayWorksMapper;
    }

    /**
     * Return a {@link List} of {@link DayWorksDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DayWorksDTO> findByCriteria(DayWorksCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DayWorks> specification = createSpecification(criteria);
        return dayWorksMapper.toDto(dayWorksRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DayWorksDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DayWorksDTO> findByCriteria(DayWorksCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DayWorks> specification = createSpecification(criteria);
        return dayWorksRepository.findAll(specification, page).map(dayWorksMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DayWorksCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DayWorks> specification = createSpecification(criteria);
        return dayWorksRepository.count(specification);
    }

    /**
     * Function to convert {@link DayWorksCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DayWorks> createSpecification(DayWorksCriteria criteria) {
        Specification<DayWorks> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DayWorks_.id));
            }
            if (criteria.getWorkID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWorkID(), DayWorks_.workID));
            }
            if (criteria.getUserID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserID(), DayWorks_.userID));
            }
            if (criteria.getDayWork() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDayWork(), DayWorks_.dayWork));
            }
            if (criteria.getMemberId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMemberId(), root -> root.join(DayWorks_.member, JoinType.LEFT).get(Member_.id))
                    );
            }
        }
        return specification;
    }
}
