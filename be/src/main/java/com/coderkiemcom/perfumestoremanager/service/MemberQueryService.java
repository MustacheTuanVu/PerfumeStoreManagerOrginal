package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.domain.*; // for static metamodels
import com.coderkiemcom.perfumestoremanager.domain.Member;
import com.coderkiemcom.perfumestoremanager.repository.MemberRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.MemberCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.MemberDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.MemberMapper;
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
 * Service for executing complex queries for {@link Member} entities in the database.
 * The main input is a {@link MemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberDTO} or a {@link Page} of {@link MemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberQueryService extends QueryService<Member> {

    private final Logger log = LoggerFactory.getLogger(MemberQueryService.class);

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    public MemberQueryService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    /**
     * Return a {@link List} of {@link MemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberDTO> findByCriteria(MemberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Member> specification = createSpecification(criteria);
        return memberMapper.toDto(memberRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberDTO> findByCriteria(MemberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Member> specification = createSpecification(criteria);
        return memberRepository.findAll(specification, page).map(memberMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Member> specification = createSpecification(criteria);
        return memberRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Member> createSpecification(MemberCriteria criteria) {
        Specification<Member> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Member_.id));
            }
            if (criteria.getUserID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserID(), Member_.userID));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Member_.name));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Member_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Member_.email));
            }
            if (criteria.getRoleID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoleID(), Member_.roleID));
            }
            if (criteria.getStoreID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoreID(), Member_.storeID));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), Member_.salary));
            }
            if (criteria.getBillsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBillsId(), root -> root.join(Member_.bills, JoinType.LEFT).get(Bills_.id))
                    );
            }
            if (criteria.getDayWorksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDayWorksId(), root -> root.join(Member_.dayWorks, JoinType.LEFT).get(DayWorks_.id))
                    );
            }
            if (criteria.getStoresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStoresId(), root -> root.join(Member_.stores, JoinType.LEFT).get(Stores_.id))
                    );
            }
            if (criteria.getRolesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRolesId(), root -> root.join(Member_.roles, JoinType.LEFT).get(Roles_.id))
                    );
            }
        }
        return specification;
    }
}
