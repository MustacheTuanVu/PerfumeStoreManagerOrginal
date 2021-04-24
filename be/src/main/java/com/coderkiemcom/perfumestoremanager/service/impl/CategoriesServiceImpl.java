package com.coderkiemcom.perfumestoremanager.service.impl;

import com.coderkiemcom.perfumestoremanager.domain.Categories;
import com.coderkiemcom.perfumestoremanager.repository.CategoriesRepository;
import com.coderkiemcom.perfumestoremanager.service.CategoriesService;
import com.coderkiemcom.perfumestoremanager.service.dto.CategoriesDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.CategoriesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categories}.
 */
@Service
@Transactional
public class CategoriesServiceImpl implements CategoriesService {

    private final Logger log = LoggerFactory.getLogger(CategoriesServiceImpl.class);

    private final CategoriesRepository categoriesRepository;

    private final CategoriesMapper categoriesMapper;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository, CategoriesMapper categoriesMapper) {
        this.categoriesRepository = categoriesRepository;
        this.categoriesMapper = categoriesMapper;
    }

    @Override
    public CategoriesDTO save(CategoriesDTO categoriesDTO) {
        log.debug("Request to save Categories : {}", categoriesDTO);
        Categories categories = categoriesMapper.toEntity(categoriesDTO);
        categories = categoriesRepository.save(categories);
        return categoriesMapper.toDto(categories);
    }

    @Override
    public Optional<CategoriesDTO> partialUpdate(CategoriesDTO categoriesDTO) {
        log.debug("Request to partially update Categories : {}", categoriesDTO);

        return categoriesRepository
            .findById(categoriesDTO.getId())
            .map(
                existingCategories -> {
                    categoriesMapper.partialUpdate(existingCategories, categoriesDTO);
                    return existingCategories;
                }
            )
            .map(categoriesRepository::save)
            .map(categoriesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoriesRepository.findAll(pageable).map(categoriesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriesDTO> findOne(Long id) {
        log.debug("Request to get Categories : {}", id);
        return categoriesRepository.findById(id).map(categoriesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categories : {}", id);
        categoriesRepository.deleteById(id);
    }
}
