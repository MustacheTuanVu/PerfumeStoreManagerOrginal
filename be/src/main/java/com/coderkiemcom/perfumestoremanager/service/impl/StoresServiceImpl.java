package com.coderkiemcom.perfumestoremanager.service.impl;

import com.coderkiemcom.perfumestoremanager.domain.Stores;
import com.coderkiemcom.perfumestoremanager.repository.StoresRepository;
import com.coderkiemcom.perfumestoremanager.service.StoresService;
import com.coderkiemcom.perfumestoremanager.service.dto.StoresDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.StoresMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Stores}.
 */
@Service
@Transactional
public class StoresServiceImpl implements StoresService {

    private final Logger log = LoggerFactory.getLogger(StoresServiceImpl.class);

    private final StoresRepository storesRepository;

    private final StoresMapper storesMapper;

    public StoresServiceImpl(StoresRepository storesRepository, StoresMapper storesMapper) {
        this.storesRepository = storesRepository;
        this.storesMapper = storesMapper;
    }

    @Override
    public StoresDTO save(StoresDTO storesDTO) {
        log.debug("Request to save Stores : {}", storesDTO);
        Stores stores = storesMapper.toEntity(storesDTO);
        stores = storesRepository.save(stores);
        return storesMapper.toDto(stores);
    }

    @Override
    public Optional<StoresDTO> partialUpdate(StoresDTO storesDTO) {
        log.debug("Request to partially update Stores : {}", storesDTO);

        return storesRepository
            .findById(storesDTO.getId())
            .map(
                existingStores -> {
                    storesMapper.partialUpdate(existingStores, storesDTO);
                    return existingStores;
                }
            )
            .map(storesRepository::save)
            .map(storesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoresDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stores");
        return storesRepository.findAll(pageable).map(storesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StoresDTO> findOne(Long id) {
        log.debug("Request to get Stores : {}", id);
        return storesRepository.findById(id).map(storesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stores : {}", id);
        storesRepository.deleteById(id);
    }
}
