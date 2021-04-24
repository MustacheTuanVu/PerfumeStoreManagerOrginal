package com.coderkiemcom.perfumestoremanager.web.rest;

import com.coderkiemcom.perfumestoremanager.repository.StoresRepository;
import com.coderkiemcom.perfumestoremanager.service.StoresQueryService;
import com.coderkiemcom.perfumestoremanager.service.StoresService;
import com.coderkiemcom.perfumestoremanager.service.criteria.StoresCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.StoresDTO;
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
 * REST controller for managing {@link com.coderkiemcom.perfumestoremanager.domain.Stores}.
 */
@RestController
@RequestMapping("/api")
public class StoresResource {

    private final Logger log = LoggerFactory.getLogger(StoresResource.class);

    private static final String ENTITY_NAME = "stores";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoresService storesService;

    private final StoresRepository storesRepository;

    private final StoresQueryService storesQueryService;

    public StoresResource(StoresService storesService, StoresRepository storesRepository, StoresQueryService storesQueryService) {
        this.storesService = storesService;
        this.storesRepository = storesRepository;
        this.storesQueryService = storesQueryService;
    }

    /**
     * {@code POST  /stores} : Create a new stores.
     *
     * @param storesDTO the storesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storesDTO, or with status {@code 400 (Bad Request)} if the stores has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stores")
    public ResponseEntity<StoresDTO> createStores(@RequestBody StoresDTO storesDTO) throws URISyntaxException {
        log.debug("REST request to save Stores : {}", storesDTO);
        if (storesDTO.getId() != null) {
            throw new BadRequestAlertException("A new stores cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoresDTO result = storesService.save(storesDTO);
        return ResponseEntity
            .created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stores/:id} : Updates an existing stores.
     *
     * @param id the id of the storesDTO to save.
     * @param storesDTO the storesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storesDTO,
     * or with status {@code 400 (Bad Request)} if the storesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stores/{id}")
    public ResponseEntity<StoresDTO> updateStores(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoresDTO storesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Stores : {}, {}", id, storesDTO);
        if (storesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StoresDTO result = storesService.save(storesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stores/:id} : Partial updates given fields of an existing stores, field will ignore if it is null
     *
     * @param id the id of the storesDTO to save.
     * @param storesDTO the storesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storesDTO,
     * or with status {@code 400 (Bad Request)} if the storesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stores/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<StoresDTO> partialUpdateStores(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoresDTO storesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Stores partially : {}, {}", id, storesDTO);
        if (storesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StoresDTO> result = storesService.partialUpdate(storesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stores} : get all the stores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stores in body.
     */
    @GetMapping("/stores")
    public ResponseEntity<List<StoresDTO>> getAllStores(StoresCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Stores by criteria: {}", criteria);
        Page<StoresDTO> page = storesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stores/count} : count all the stores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/stores/count")
    public ResponseEntity<Long> countStores(StoresCriteria criteria) {
        log.debug("REST request to count Stores by criteria: {}", criteria);
        return ResponseEntity.ok().body(storesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stores/:id} : get the "id" stores.
     *
     * @param id the id of the storesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stores/{id}")
    public ResponseEntity<StoresDTO> getStores(@PathVariable Long id) {
        log.debug("REST request to get Stores : {}", id);
        Optional<StoresDTO> storesDTO = storesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storesDTO);
    }

    /**
     * {@code DELETE  /stores/:id} : delete the "id" stores.
     *
     * @param id the id of the storesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Void> deleteStores(@PathVariable Long id) {
        log.debug("REST request to delete Stores : {}", id);
        storesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
