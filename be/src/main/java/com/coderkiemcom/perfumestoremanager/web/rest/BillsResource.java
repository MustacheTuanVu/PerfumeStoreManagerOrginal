package com.coderkiemcom.perfumestoremanager.web.rest;

import com.coderkiemcom.perfumestoremanager.repository.BillsRepository;
import com.coderkiemcom.perfumestoremanager.service.BillsQueryService;
import com.coderkiemcom.perfumestoremanager.service.BillsService;
import com.coderkiemcom.perfumestoremanager.service.criteria.BillsCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.BillsDTO;
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
 * REST controller for managing {@link com.coderkiemcom.perfumestoremanager.domain.Bills}.
 */
@RestController
@RequestMapping("/api")
public class BillsResource {

    private final Logger log = LoggerFactory.getLogger(BillsResource.class);

    private static final String ENTITY_NAME = "bills";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillsService billsService;

    private final BillsRepository billsRepository;

    private final BillsQueryService billsQueryService;

    public BillsResource(BillsService billsService, BillsRepository billsRepository, BillsQueryService billsQueryService) {
        this.billsService = billsService;
        this.billsRepository = billsRepository;
        this.billsQueryService = billsQueryService;
    }

    /**
     * {@code POST  /bills} : Create a new bills.
     *
     * @param billsDTO the billsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billsDTO, or with status {@code 400 (Bad Request)} if the bills has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bills")
    public ResponseEntity<BillsDTO> createBills(@RequestBody BillsDTO billsDTO) throws URISyntaxException {
        log.debug("REST request to save Bills : {}", billsDTO);
        if (billsDTO.getId() != null) {
            throw new BadRequestAlertException("A new bills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillsDTO result = billsService.save(billsDTO);
        return ResponseEntity
            .created(new URI("/api/bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bills/:id} : Updates an existing bills.
     *
     * @param id the id of the billsDTO to save.
     * @param billsDTO the billsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billsDTO,
     * or with status {@code 400 (Bad Request)} if the billsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bills/{id}")
    public ResponseEntity<BillsDTO> updateBills(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillsDTO billsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Bills : {}, {}", id, billsDTO);
        if (billsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillsDTO result = billsService.save(billsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bills/:id} : Partial updates given fields of an existing bills, field will ignore if it is null
     *
     * @param id the id of the billsDTO to save.
     * @param billsDTO the billsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billsDTO,
     * or with status {@code 400 (Bad Request)} if the billsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bills/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BillsDTO> partialUpdateBills(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillsDTO billsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bills partially : {}, {}", id, billsDTO);
        if (billsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillsDTO> result = billsService.partialUpdate(billsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bills} : get all the bills.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bills in body.
     */
    @GetMapping("/bills")
    public ResponseEntity<List<BillsDTO>> getAllBills(BillsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Bills by criteria: {}", criteria);
        Page<BillsDTO> page = billsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bills/count} : count all the bills.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bills/count")
    public ResponseEntity<Long> countBills(BillsCriteria criteria) {
        log.debug("REST request to count Bills by criteria: {}", criteria);
        return ResponseEntity.ok().body(billsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bills/:id} : get the "id" bills.
     *
     * @param id the id of the billsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bills/{id}")
    public ResponseEntity<BillsDTO> getBills(@PathVariable Long id) {
        log.debug("REST request to get Bills : {}", id);
        Optional<BillsDTO> billsDTO = billsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billsDTO);
    }

    /**
     * {@code DELETE  /bills/:id} : delete the "id" bills.
     *
     * @param id the id of the billsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<Void> deleteBills(@PathVariable Long id) {
        log.debug("REST request to delete Bills : {}", id);
        billsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
