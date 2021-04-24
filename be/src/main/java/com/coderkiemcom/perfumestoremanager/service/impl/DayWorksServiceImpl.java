package com.coderkiemcom.perfumestoremanager.service.impl;

import com.coderkiemcom.perfumestoremanager.domain.DayWorks;
import com.coderkiemcom.perfumestoremanager.repository.DayWorksRepository;
import com.coderkiemcom.perfumestoremanager.service.DayWorksService;
import com.coderkiemcom.perfumestoremanager.service.dto.DayWorksDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.DayWorksMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DayWorks}.
 */
@Service
@Transactional
public class DayWorksServiceImpl implements DayWorksService {

    private final Logger log = LoggerFactory.getLogger(DayWorksServiceImpl.class);

    private final DayWorksRepository dayWorksRepository;

    private final DayWorksMapper dayWorksMapper;

    public DayWorksServiceImpl(DayWorksRepository dayWorksRepository, DayWorksMapper dayWorksMapper) {
        this.dayWorksRepository = dayWorksRepository;
        this.dayWorksMapper = dayWorksMapper;
    }

    @Override
    public DayWorksDTO save(DayWorksDTO dayWorksDTO) {
        log.debug("Request to save DayWorks : {}", dayWorksDTO);
        DayWorks dayWorks = dayWorksMapper.toEntity(dayWorksDTO);
        dayWorks = dayWorksRepository.save(dayWorks);
        return dayWorksMapper.toDto(dayWorks);
    }

    @Override
    public Optional<DayWorksDTO> partialUpdate(DayWorksDTO dayWorksDTO) {
        log.debug("Request to partially update DayWorks : {}", dayWorksDTO);

        return dayWorksRepository
            .findById(dayWorksDTO.getId())
            .map(
                existingDayWorks -> {
                    dayWorksMapper.partialUpdate(existingDayWorks, dayWorksDTO);
                    return existingDayWorks;
                }
            )
            .map(dayWorksRepository::save)
            .map(dayWorksMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DayWorksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DayWorks");
        return dayWorksRepository.findAll(pageable).map(dayWorksMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DayWorksDTO> findOne(Long id) {
        log.debug("Request to get DayWorks : {}", id);
        return dayWorksRepository.findById(id).map(dayWorksMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DayWorks : {}", id);
        dayWorksRepository.deleteById(id);
    }
}
