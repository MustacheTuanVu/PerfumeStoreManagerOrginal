package com.coderkiemcom.perfumestoremanager.web.rest;

import com.coderkiemcom.perfumestoremanager.repository.DayWorksRepository;
import com.coderkiemcom.perfumestoremanager.service.DayWorksQueryService;
import com.coderkiemcom.perfumestoremanager.service.DayWorksService;
import com.coderkiemcom.perfumestoremanager.service.criteria.DayWorksCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.DayWorksDTO;
import com.coderkiemcom.perfumestoremanager.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.coderkiemcom.perfumestoremanager.domain.DayWorks}.
 */
@RestController
@RequestMapping("/api")
public class DayWorksResource {

    private final Logger log = LoggerFactory.getLogger(DayWorksResource.class);

    private static final String ENTITY_NAME = "dayWorks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DayWorksService dayWorksService;

    private final DayWorksRepository dayWorksRepository;

    private final DayWorksQueryService dayWorksQueryService;

    public DayWorksResource(
        DayWorksService dayWorksService,
        DayWorksRepository dayWorksRepository,
        DayWorksQueryService dayWorksQueryService
    ) {
        this.dayWorksService = dayWorksService;
        this.dayWorksRepository = dayWorksRepository;
        this.dayWorksQueryService = dayWorksQueryService;
    }

    /**
     * {@code POST  /day-works} : Create a new dayWorks.
     *
     * @param dayWorksDTO the dayWorksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dayWorksDTO, or with status {@code 400 (Bad Request)} if the dayWorks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/day-works")
    public ResponseEntity<DayWorksDTO> createDayWorks(@RequestBody DayWorksDTO dayWorksDTO) throws URISyntaxException {
        log.debug("REST request to save DayWorks : {}", dayWorksDTO);
        if (dayWorksDTO.getId() != null) {
            throw new BadRequestAlertException("A new dayWorks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DayWorksDTO result = dayWorksService.save(dayWorksDTO);
        return ResponseEntity
            .created(new URI("/api/day-works/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /day-works/:id} : Updates an existing dayWorks.
     *
     * @param id the id of the dayWorksDTO to save.
     * @param dayWorksDTO the dayWorksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dayWorksDTO,
     * or with status {@code 400 (Bad Request)} if the dayWorksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dayWorksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/day-works/{id}")
    public ResponseEntity<DayWorksDTO> updateDayWorks(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DayWorksDTO dayWorksDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DayWorks : {}, {}", id, dayWorksDTO);
        if (dayWorksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dayWorksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dayWorksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DayWorksDTO result = dayWorksService.save(dayWorksDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dayWorksDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /day-works/:id} : Partial updates given fields of an existing dayWorks, field will ignore if it is null
     *
     * @param id the id of the dayWorksDTO to save.
     * @param dayWorksDTO the dayWorksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dayWorksDTO,
     * or with status {@code 400 (Bad Request)} if the dayWorksDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dayWorksDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dayWorksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/day-works/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DayWorksDTO> partialUpdateDayWorks(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DayWorksDTO dayWorksDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DayWorks partially : {}, {}", id, dayWorksDTO);
        if (dayWorksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dayWorksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dayWorksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DayWorksDTO> result = dayWorksService.partialUpdate(dayWorksDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dayWorksDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /day-works} : get all the dayWorks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dayWorks in body.
     */
    @GetMapping("/day-works")
    public ResponseEntity<List<DayWorksDTO>> getAllDayWorks(DayWorksCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DayWorks by criteria: {}", criteria);
        Page<DayWorksDTO> page = dayWorksQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /day-works/count} : count all the dayWorks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/day-works/count")
    public ResponseEntity<Long> countDayWorks(DayWorksCriteria criteria) {
        log.debug("REST request to count DayWorks by criteria: {}", criteria);
        return ResponseEntity.ok().body(dayWorksQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /day-works/:id} : get the "id" dayWorks.
     *
     * @param id the id of the dayWorksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dayWorksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/day-works/{id}")
    public ResponseEntity<DayWorksDTO> getDayWorks(@PathVariable Long id) {
        log.debug("REST request to get DayWorks : {}", id);
        Optional<DayWorksDTO> dayWorksDTO = dayWorksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dayWorksDTO);
    }

    /**
     * {@code DELETE  /day-works/:id} : delete the "id" dayWorks.
     *
     * @param id the id of the dayWorksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/day-works/{id}")
    public ResponseEntity<Void> deleteDayWorks(@PathVariable Long id) {
        log.debug("REST request to delete DayWorks : {}", id);
        dayWorksService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
