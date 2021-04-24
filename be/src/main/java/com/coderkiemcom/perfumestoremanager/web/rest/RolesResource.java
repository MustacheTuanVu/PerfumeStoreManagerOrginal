package com.coderkiemcom.perfumestoremanager.web.rest;

import com.coderkiemcom.perfumestoremanager.repository.RolesRepository;
import com.coderkiemcom.perfumestoremanager.service.RolesQueryService;
import com.coderkiemcom.perfumestoremanager.service.RolesService;
import com.coderkiemcom.perfumestoremanager.service.criteria.RolesCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.RolesDTO;
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
 * REST controller for managing {@link com.coderkiemcom.perfumestoremanager.domain.Roles}.
 */
@RestController
@RequestMapping("/api")
public class RolesResource {

    private final Logger log = LoggerFactory.getLogger(RolesResource.class);

    private static final String ENTITY_NAME = "roles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RolesService rolesService;

    private final RolesRepository rolesRepository;

    private final RolesQueryService rolesQueryService;

    public RolesResource(RolesService rolesService, RolesRepository rolesRepository, RolesQueryService rolesQueryService) {
        this.rolesService = rolesService;
        this.rolesRepository = rolesRepository;
        this.rolesQueryService = rolesQueryService;
    }

    /**
     * {@code POST  /roles} : Create a new roles.
     *
     * @param rolesDTO the rolesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rolesDTO, or with status {@code 400 (Bad Request)} if the roles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles")
    public ResponseEntity<RolesDTO> createRoles(@RequestBody RolesDTO rolesDTO) throws URISyntaxException {
        log.debug("REST request to save Roles : {}", rolesDTO);
        if (rolesDTO.getId() != null) {
            throw new BadRequestAlertException("A new roles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RolesDTO result = rolesService.save(rolesDTO);
        return ResponseEntity
            .created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roles/:id} : Updates an existing roles.
     *
     * @param id the id of the rolesDTO to save.
     * @param rolesDTO the rolesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolesDTO,
     * or with status {@code 400 (Bad Request)} if the rolesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rolesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/roles/{id}")
    public ResponseEntity<RolesDTO> updateRoles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RolesDTO rolesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Roles : {}, {}", id, rolesDTO);
        if (rolesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RolesDTO result = rolesService.save(rolesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rolesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roles/:id} : Partial updates given fields of an existing roles, field will ignore if it is null
     *
     * @param id the id of the rolesDTO to save.
     * @param rolesDTO the rolesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolesDTO,
     * or with status {@code 400 (Bad Request)} if the rolesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rolesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rolesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/roles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RolesDTO> partialUpdateRoles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RolesDTO rolesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Roles partially : {}, {}", id, rolesDTO);
        if (rolesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RolesDTO> result = rolesService.partialUpdate(rolesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rolesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /roles} : get all the roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
     */
    @GetMapping("/roles")
    public ResponseEntity<List<RolesDTO>> getAllRoles(RolesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Roles by criteria: {}", criteria);
        Page<RolesDTO> page = rolesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roles/count} : count all the roles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/roles/count")
    public ResponseEntity<Long> countRoles(RolesCriteria criteria) {
        log.debug("REST request to count Roles by criteria: {}", criteria);
        return ResponseEntity.ok().body(rolesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /roles/:id} : get the "id" roles.
     *
     * @param id the id of the rolesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rolesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<RolesDTO> getRoles(@PathVariable Long id) {
        log.debug("REST request to get Roles : {}", id);
        Optional<RolesDTO> rolesDTO = rolesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rolesDTO);
    }

    /**
     * {@code DELETE  /roles/:id} : delete the "id" roles.
     *
     * @param id the id of the rolesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRoles(@PathVariable Long id) {
        log.debug("REST request to delete Roles : {}", id);
        rolesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
