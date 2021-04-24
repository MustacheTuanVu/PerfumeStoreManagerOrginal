package com.coderkiemcom.perfumestoremanager.service.impl;

import com.coderkiemcom.perfumestoremanager.domain.Customers;
import com.coderkiemcom.perfumestoremanager.repository.CustomersRepository;
import com.coderkiemcom.perfumestoremanager.service.CustomersService;
import com.coderkiemcom.perfumestoremanager.service.dto.CustomersDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.CustomersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Customers}.
 */
@Service
@Transactional
public class CustomersServiceImpl implements CustomersService {

    private final Logger log = LoggerFactory.getLogger(CustomersServiceImpl.class);

    private final CustomersRepository customersRepository;

    private final CustomersMapper customersMapper;

    public CustomersServiceImpl(CustomersRepository customersRepository, CustomersMapper customersMapper) {
        this.customersRepository = customersRepository;
        this.customersMapper = customersMapper;
    }

    @Override
    public CustomersDTO save(CustomersDTO customersDTO) {
        log.debug("Request to save Customers : {}", customersDTO);
        Customers customers = customersMapper.toEntity(customersDTO);
        customers = customersRepository.save(customers);
        return customersMapper.toDto(customers);
    }

    @Override
    public Optional<CustomersDTO> partialUpdate(CustomersDTO customersDTO) {
        log.debug("Request to partially update Customers : {}", customersDTO);

        return customersRepository
            .findById(customersDTO.getId())
            .map(
                existingCustomers -> {
                    customersMapper.partialUpdate(existingCustomers, customersDTO);
                    return existingCustomers;
                }
            )
            .map(customersRepository::save)
            .map(customersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        return customersRepository.findAll(pageable).map(customersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomersDTO> findOne(Long id) {
        log.debug("Request to get Customers : {}", id);
        return customersRepository.findById(id).map(customersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Customers : {}", id);
        customersRepository.deleteById(id);
    }
}
