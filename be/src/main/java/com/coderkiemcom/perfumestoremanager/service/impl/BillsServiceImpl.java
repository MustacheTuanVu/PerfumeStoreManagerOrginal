package com.coderkiemcom.perfumestoremanager.service.impl;

import com.coderkiemcom.perfumestoremanager.domain.Bills;
import com.coderkiemcom.perfumestoremanager.repository.BillsRepository;
import com.coderkiemcom.perfumestoremanager.service.BillsService;
import com.coderkiemcom.perfumestoremanager.service.dto.BillsDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.BillsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bills}.
 */
@Service
@Transactional
public class BillsServiceImpl implements BillsService {

    private final Logger log = LoggerFactory.getLogger(BillsServiceImpl.class);

    private final BillsRepository billsRepository;

    private final BillsMapper billsMapper;

    public BillsServiceImpl(BillsRepository billsRepository, BillsMapper billsMapper) {
        this.billsRepository = billsRepository;
        this.billsMapper = billsMapper;
    }

    @Override
    public BillsDTO save(BillsDTO billsDTO) {
        log.debug("Request to save Bills : {}", billsDTO);
        Bills bills = billsMapper.toEntity(billsDTO);
        bills = billsRepository.save(bills);
        return billsMapper.toDto(bills);
    }

    @Override
    public Optional<BillsDTO> partialUpdate(BillsDTO billsDTO) {
        log.debug("Request to partially update Bills : {}", billsDTO);

        return billsRepository
            .findById(billsDTO.getId())
            .map(
                existingBills -> {
                    billsMapper.partialUpdate(existingBills, billsDTO);
                    return existingBills;
                }
            )
            .map(billsRepository::save)
            .map(billsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bills");
        return billsRepository.findAll(pageable).map(billsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillsDTO> findOne(Long id) {
        log.debug("Request to get Bills : {}", id);
        return billsRepository.findById(id).map(billsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bills : {}", id);
        billsRepository.deleteById(id);
    }
}
