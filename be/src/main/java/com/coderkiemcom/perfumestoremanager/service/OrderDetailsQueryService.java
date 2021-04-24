package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.domain.*; // for static metamodels
import com.coderkiemcom.perfumestoremanager.domain.OrderDetails;
import com.coderkiemcom.perfumestoremanager.repository.OrderDetailsRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.OrderDetailsCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.OrderDetailsDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.OrderDetailsMapper;
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
 * Service for executing complex queries for {@link OrderDetails} entities in the database.
 * The main input is a {@link OrderDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderDetailsDTO} or a {@link Page} of {@link OrderDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderDetailsQueryService extends QueryService<OrderDetails> {

    private final Logger log = LoggerFactory.getLogger(OrderDetailsQueryService.class);

    private final OrderDetailsRepository orderDetailsRepository;

    private final OrderDetailsMapper orderDetailsMapper;

    public OrderDetailsQueryService(OrderDetailsRepository orderDetailsRepository, OrderDetailsMapper orderDetailsMapper) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderDetailsMapper = orderDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link OrderDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDetailsDTO> findByCriteria(OrderDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderDetails> specification = createSpecification(criteria);
        return orderDetailsMapper.toDto(orderDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDetailsDTO> findByCriteria(OrderDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderDetails> specification = createSpecification(criteria);
        return orderDetailsRepository.findAll(specification, page).map(orderDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderDetails> specification = createSpecification(criteria);
        return orderDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderDetails> createSpecification(OrderDetailsCriteria criteria) {
        Specification<OrderDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderDetails_.id));
            }
            if (criteria.getOrderID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderID(), OrderDetails_.orderID));
            }
            if (criteria.getBillID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBillID(), OrderDetails_.billID));
            }
            if (criteria.getProductID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductID(), OrderDetails_.productID));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), OrderDetails_.quantity));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), OrderDetails_.price));
            }
            if (criteria.getBillsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBillsId(), root -> root.join(OrderDetails_.bills, JoinType.LEFT).get(Bills_.id))
                    );
            }
            if (criteria.getProductsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductsId(),
                            root -> root.join(OrderDetails_.products, JoinType.LEFT).get(Products_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
