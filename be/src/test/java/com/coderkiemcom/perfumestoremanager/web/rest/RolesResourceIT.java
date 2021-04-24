package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Member;
import com.coderkiemcom.perfumestoremanager.domain.Roles;
import com.coderkiemcom.perfumestoremanager.repository.RolesRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.RolesCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.RolesDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.RolesMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RolesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RolesResourceIT {

    private static final String DEFAULT_ROLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolesMockMvc;

    private Roles roles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roles createEntity(EntityManager em) {
        Roles roles = new Roles().roleID(DEFAULT_ROLE_ID).roleName(DEFAULT_ROLE_NAME);
        return roles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roles createUpdatedEntity(EntityManager em) {
        Roles roles = new Roles().roleID(UPDATED_ROLE_ID).roleName(UPDATED_ROLE_NAME);
        return roles;
    }

    @BeforeEach
    public void initTest() {
        roles = createEntity(em);
    }

    @Test
    @Transactional
    void createRoles() throws Exception {
        int databaseSizeBeforeCreate = rolesRepository.findAll().size();
        // Create the Roles
        RolesDTO rolesDTO = rolesMapper.toDto(roles);
        restRolesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolesDTO)))
            .andExpect(status().isCreated());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeCreate + 1);
        Roles testRoles = rolesList.get(rolesList.size() - 1);
        assertThat(testRoles.getRoleID()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testRoles.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
    }

    @Test
    @Transactional
    void createRolesWithExistingId() throws Exception {
        // Create the Roles with an existing ID
        roles.setId(1L);
        RolesDTO rolesDTO = rolesMapper.toDto(roles);

        int databaseSizeBeforeCreate = rolesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList
        restRolesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roles.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleID").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)));
    }

    @Test
    @Transactional
    void getRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get the roles
        restRolesMockMvc
            .perform(get(ENTITY_API_URL_ID, roles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roles.getId().intValue()))
            .andExpect(jsonPath("$.roleID").value(DEFAULT_ROLE_ID))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME));
    }

    @Test
    @Transactional
    void getRolesByIdFiltering() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        Long id = roles.getId();

        defaultRolesShouldBeFound("id.equals=" + id);
        defaultRolesShouldNotBeFound("id.notEquals=" + id);

        defaultRolesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRolesShouldNotBeFound("id.greaterThan=" + id);

        defaultRolesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRolesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRolesByRoleIDIsEqualToSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleID equals to DEFAULT_ROLE_ID
        defaultRolesShouldBeFound("roleID.equals=" + DEFAULT_ROLE_ID);

        // Get all the rolesList where roleID equals to UPDATED_ROLE_ID
        defaultRolesShouldNotBeFound("roleID.equals=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllRolesByRoleIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleID not equals to DEFAULT_ROLE_ID
        defaultRolesShouldNotBeFound("roleID.notEquals=" + DEFAULT_ROLE_ID);

        // Get all the rolesList where roleID not equals to UPDATED_ROLE_ID
        defaultRolesShouldBeFound("roleID.notEquals=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllRolesByRoleIDIsInShouldWork() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleID in DEFAULT_ROLE_ID or UPDATED_ROLE_ID
        defaultRolesShouldBeFound("roleID.in=" + DEFAULT_ROLE_ID + "," + UPDATED_ROLE_ID);

        // Get all the rolesList where roleID equals to UPDATED_ROLE_ID
        defaultRolesShouldNotBeFound("roleID.in=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllRolesByRoleIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleID is not null
        defaultRolesShouldBeFound("roleID.specified=true");

        // Get all the rolesList where roleID is null
        defaultRolesShouldNotBeFound("roleID.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByRoleIDContainsSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleID contains DEFAULT_ROLE_ID
        defaultRolesShouldBeFound("roleID.contains=" + DEFAULT_ROLE_ID);

        // Get all the rolesList where roleID contains UPDATED_ROLE_ID
        defaultRolesShouldNotBeFound("roleID.contains=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllRolesByRoleIDNotContainsSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleID does not contain DEFAULT_ROLE_ID
        defaultRolesShouldNotBeFound("roleID.doesNotContain=" + DEFAULT_ROLE_ID);

        // Get all the rolesList where roleID does not contain UPDATED_ROLE_ID
        defaultRolesShouldBeFound("roleID.doesNotContain=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleName equals to DEFAULT_ROLE_NAME
        defaultRolesShouldBeFound("roleName.equals=" + DEFAULT_ROLE_NAME);

        // Get all the rolesList where roleName equals to UPDATED_ROLE_NAME
        defaultRolesShouldNotBeFound("roleName.equals=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleName not equals to DEFAULT_ROLE_NAME
        defaultRolesShouldNotBeFound("roleName.notEquals=" + DEFAULT_ROLE_NAME);

        // Get all the rolesList where roleName not equals to UPDATED_ROLE_NAME
        defaultRolesShouldBeFound("roleName.notEquals=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameIsInShouldWork() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleName in DEFAULT_ROLE_NAME or UPDATED_ROLE_NAME
        defaultRolesShouldBeFound("roleName.in=" + DEFAULT_ROLE_NAME + "," + UPDATED_ROLE_NAME);

        // Get all the rolesList where roleName equals to UPDATED_ROLE_NAME
        defaultRolesShouldNotBeFound("roleName.in=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleName is not null
        defaultRolesShouldBeFound("roleName.specified=true");

        // Get all the rolesList where roleName is null
        defaultRolesShouldNotBeFound("roleName.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameContainsSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleName contains DEFAULT_ROLE_NAME
        defaultRolesShouldBeFound("roleName.contains=" + DEFAULT_ROLE_NAME);

        // Get all the rolesList where roleName contains UPDATED_ROLE_NAME
        defaultRolesShouldNotBeFound("roleName.contains=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameNotContainsSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList where roleName does not contain DEFAULT_ROLE_NAME
        defaultRolesShouldNotBeFound("roleName.doesNotContain=" + DEFAULT_ROLE_NAME);

        // Get all the rolesList where roleName does not contain UPDATED_ROLE_NAME
        defaultRolesShouldBeFound("roleName.doesNotContain=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);
        Member member = MemberResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        roles.addMember(member);
        rolesRepository.saveAndFlush(roles);
        Long memberId = member.getId();

        // Get all the rolesList where member equals to memberId
        defaultRolesShouldBeFound("memberId.equals=" + memberId);

        // Get all the rolesList where member equals to (memberId + 1)
        defaultRolesShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRolesShouldBeFound(String filter) throws Exception {
        restRolesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roles.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleID").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)));

        // Check, that the count call also returns 1
        restRolesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRolesShouldNotBeFound(String filter) throws Exception {
        restRolesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRolesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoles() throws Exception {
        // Get the roles
        restRolesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

        // Update the roles
        Roles updatedRoles = rolesRepository.findById(roles.getId()).get();
        // Disconnect from session so that the updates on updatedRoles are not directly saved in db
        em.detach(updatedRoles);
        updatedRoles.roleID(UPDATED_ROLE_ID).roleName(UPDATED_ROLE_NAME);
        RolesDTO rolesDTO = rolesMapper.toDto(updatedRoles);

        restRolesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rolesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rolesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
        Roles testRoles = rolesList.get(rolesList.size() - 1);
        assertThat(testRoles.getRoleID()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testRoles.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRoles() throws Exception {
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
        roles.setId(count.incrementAndGet());

        // Create the Roles
        RolesDTO rolesDTO = rolesMapper.toDto(roles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rolesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rolesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoles() throws Exception {
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
        roles.setId(count.incrementAndGet());

        // Create the Roles
        RolesDTO rolesDTO = rolesMapper.toDto(roles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rolesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoles() throws Exception {
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
        roles.setId(count.incrementAndGet());

        // Create the Roles
        RolesDTO rolesDTO = rolesMapper.toDto(roles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRolesWithPatch() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

        // Update the roles using partial update
        Roles partialUpdatedRoles = new Roles();
        partialUpdatedRoles.setId(roles.getId());

        partialUpdatedRoles.roleID(UPDATED_ROLE_ID).roleName(UPDATED_ROLE_NAME);

        restRolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoles))
            )
            .andExpect(status().isOk());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
        Roles testRoles = rolesList.get(rolesList.size() - 1);
        assertThat(testRoles.getRoleID()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testRoles.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRolesWithPatch() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

        // Update the roles using partial update
        Roles partialUpdatedRoles = new Roles();
        partialUpdatedRoles.setId(roles.getId());

        partialUpdatedRoles.roleID(UPDATED_ROLE_ID).roleName(UPDATED_ROLE_NAME);

        restRolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoles))
            )
            .andExpect(status().isOk());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
        Roles testRoles = rolesList.get(rolesList.size() - 1);
        assertThat(testRoles.getRoleID()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testRoles.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRoles() throws Exception {
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
        roles.setId(count.incrementAndGet());

        // Create the Roles
        RolesDTO rolesDTO = rolesMapper.toDto(roles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rolesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rolesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoles() throws Exception {
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
        roles.setId(count.incrementAndGet());

        // Create the Roles
        RolesDTO rolesDTO = rolesMapper.toDto(roles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rolesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoles() throws Exception {
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
        roles.setId(count.incrementAndGet());

        // Create the Roles
        RolesDTO rolesDTO = rolesMapper.toDto(roles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rolesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        int databaseSizeBeforeDelete = rolesRepository.findAll().size();

        // Delete the roles
        restRolesMockMvc
            .perform(delete(ENTITY_API_URL_ID, roles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
