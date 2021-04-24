package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.domain.*; // for static metamodels
import com.coderkiemcom.perfumestoremanager.domain.Bills;
import com.coderkiemcom.perfumestoremanager.repository.BillsRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.BillsCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.BillsDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.BillsMapper;
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
 * Service for executing complex queries for {@link Bills} entities in the database.
 * The main input is a {@link BillsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BillsDTO} or a {@link Page} of {@link BillsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BillsQueryService extends QueryService<Bills> {

    private final Logger log = LoggerFactory.getLogger(BillsQueryService.class);

    private final BillsRepository billsRepository;

    private final BillsMapper billsMapper;

    public BillsQueryService(BillsRepository billsRepository, BillsMapper billsMapper) {
        this.billsRepository = billsRepository;
        this.billsMapper = billsMapper;
    }

    /**
     * Return a {@link List} of {@link BillsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillsDTO> findByCriteria(BillsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Bills> specification = createSpecification(criteria);
        return billsMapper.toDto(billsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BillsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillsDTO> findByCriteria(BillsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bills> specification = createSpecification(criteria);
        return billsRepository.findAll(specification, page).map(billsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BillsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Bills> specification = createSpecification(criteria);
        return billsRepository.count(specification);
    }

    /**
     * Function to convert {@link BillsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bills> createSpecification(BillsCriteria criteria) {
        Specification<Bills> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bills_.id));
            }
            if (criteria.getBillID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBillID(), Bills_.billID));
            }
            if (criteria.getSalesID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSalesID(), Bills_.salesID));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Bills_.date));
            }
            if (criteria.getDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscount(), Bills_.discount));
            }
            if (criteria.getVat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVat(), Bills_.vat));
            }
            if (criteria.getPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayment(), Bills_.payment));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Bills_.total));
            }
            if (criteria.getCustomerID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerID(), Bills_.customerID));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Bills_.status));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Bills_.description));
            }
            if (criteria.getOrderDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderDetailsId(),
                            root -> root.join(Bills_.orderDetails, JoinType.LEFT).get(OrderDetails_.id)
                        )
                    );
            }
            if (criteria.getCustomersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCustomersId(), root -> root.join(Bills_.customers, JoinType.LEFT).get(Customers_.id))
                    );
            }
            if (criteria.getMemberId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMemberId(), root -> root.join(Bills_.member, JoinType.LEFT).get(Member_.id))
                    );
            }
        }
        return specification;
    }
}
