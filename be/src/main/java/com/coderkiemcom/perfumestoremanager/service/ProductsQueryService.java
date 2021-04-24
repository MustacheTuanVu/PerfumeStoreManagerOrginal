package com.coderkiemcom.perfumestoremanager.service;

import com.coderkiemcom.perfumestoremanager.domain.*; // for static metamodels
import com.coderkiemcom.perfumestoremanager.domain.Products;
import com.coderkiemcom.perfumestoremanager.repository.ProductsRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.ProductsCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.ProductsDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.ProductsMapper;
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
 * Service for executing complex queries for {@link Products} entities in the database.
 * The main input is a {@link ProductsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductsDTO} or a {@link Page} of {@link ProductsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductsQueryService extends QueryService<Products> {

    private final Logger log = LoggerFactory.getLogger(ProductsQueryService.class);

    private final ProductsRepository productsRepository;

    private final ProductsMapper productsMapper;

    public ProductsQueryService(ProductsRepository productsRepository, ProductsMapper productsMapper) {
        this.productsRepository = productsRepository;
        this.productsMapper = productsMapper;
    }

    /**
     * Return a {@link List} of {@link ProductsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductsDTO> findByCriteria(ProductsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Products> specification = createSpecification(criteria);
        return productsMapper.toDto(productsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductsDTO> findByCriteria(ProductsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Products> specification = createSpecification(criteria);
        return productsRepository.findAll(specification, page).map(productsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Products> specification = createSpecification(criteria);
        return productsRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Products> createSpecification(ProductsCriteria criteria) {
        Specification<Products> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Products_.id));
            }
            if (criteria.getProductID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductID(), Products_.productID));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), Products_.productName));
            }
            if (criteria.getQuantityAvailable() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityAvailable(), Products_.quantityAvailable));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Products_.price));
            }
            if (criteria.getDateImport() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateImport(), Products_.dateImport));
            }
            if (criteria.getExpireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpireDate(), Products_.expireDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Products_.description));
            }
            if (criteria.getCategoryID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoryID(), Products_.categoryID));
            }
            if (criteria.getVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVolume(), Products_.volume));
            }
            if (criteria.getOrderDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderDetailsId(),
                            root -> root.join(Products_.orderDetails, JoinType.LEFT).get(OrderDetails_.id)
                        )
                    );
            }
            if (criteria.getCategoriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriesId(),
                            root -> root.join(Products_.categories, JoinType.LEFT).get(Categories_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
