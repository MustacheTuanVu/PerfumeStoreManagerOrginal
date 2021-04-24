package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.DayWorks;
import com.coderkiemcom.perfumestoremanager.domain.Member;
import com.coderkiemcom.perfumestoremanager.repository.DayWorksRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.DayWorksCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.DayWorksDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.DayWorksMapper;
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
 * Integration tests for the {@link DayWorksResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DayWorksResourceIT {

    private static final String DEFAULT_WORK_ID = "AAAAAAAAAA";
    private static final String UPDATED_WORK_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAY_WORK = 1;
    private static final Integer UPDATED_DAY_WORK = 2;
    private static final Integer SMALLER_DAY_WORK = 1 - 1;

    private static final String ENTITY_API_URL = "/api/day-works";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DayWorksRepository dayWorksRepository;

    @Autowired
    private DayWorksMapper dayWorksMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDayWorksMockMvc;

    private DayWorks dayWorks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DayWorks createEntity(EntityManager em) {
        DayWorks dayWorks = new DayWorks().workID(DEFAULT_WORK_ID).userID(DEFAULT_USER_ID).dayWork(DEFAULT_DAY_WORK);
        return dayWorks;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DayWorks createUpdatedEntity(EntityManager em) {
        DayWorks dayWorks = new DayWorks().workID(UPDATED_WORK_ID).userID(UPDATED_USER_ID).dayWork(UPDATED_DAY_WORK);
        return dayWorks;
    }

    @BeforeEach
    public void initTest() {
        dayWorks = createEntity(em);
    }

    @Test
    @Transactional
    void createDayWorks() throws Exception {
        int databaseSizeBeforeCreate = dayWorksRepository.findAll().size();
        // Create the DayWorks
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);
        restDayWorksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayWorksDTO)))
            .andExpect(status().isCreated());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeCreate + 1);
        DayWorks testDayWorks = dayWorksList.get(dayWorksList.size() - 1);
        assertThat(testDayWorks.getWorkID()).isEqualTo(DEFAULT_WORK_ID);
        assertThat(testDayWorks.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testDayWorks.getDayWork()).isEqualTo(DEFAULT_DAY_WORK);
    }

    @Test
    @Transactional
    void createDayWorksWithExistingId() throws Exception {
        // Create the DayWorks with an existing ID
        dayWorks.setId(1L);
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);

        int databaseSizeBeforeCreate = dayWorksRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDayWorksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayWorksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDayWorks() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList
        restDayWorksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dayWorks.getId().intValue())))
            .andExpect(jsonPath("$.[*].workID").value(hasItem(DEFAULT_WORK_ID)))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].dayWork").value(hasItem(DEFAULT_DAY_WORK)));
    }

    @Test
    @Transactional
    void getDayWorks() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get the dayWorks
        restDayWorksMockMvc
            .perform(get(ENTITY_API_URL_ID, dayWorks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dayWorks.getId().intValue()))
            .andExpect(jsonPath("$.workID").value(DEFAULT_WORK_ID))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.dayWork").value(DEFAULT_DAY_WORK));
    }

    @Test
    @Transactional
    void getDayWorksByIdFiltering() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        Long id = dayWorks.getId();

        defaultDayWorksShouldBeFound("id.equals=" + id);
        defaultDayWorksShouldNotBeFound("id.notEquals=" + id);

        defaultDayWorksShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDayWorksShouldNotBeFound("id.greaterThan=" + id);

        defaultDayWorksShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDayWorksShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDayWorksByWorkIDIsEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where workID equals to DEFAULT_WORK_ID
        defaultDayWorksShouldBeFound("workID.equals=" + DEFAULT_WORK_ID);

        // Get all the dayWorksList where workID equals to UPDATED_WORK_ID
        defaultDayWorksShouldNotBeFound("workID.equals=" + UPDATED_WORK_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByWorkIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where workID not equals to DEFAULT_WORK_ID
        defaultDayWorksShouldNotBeFound("workID.notEquals=" + DEFAULT_WORK_ID);

        // Get all the dayWorksList where workID not equals to UPDATED_WORK_ID
        defaultDayWorksShouldBeFound("workID.notEquals=" + UPDATED_WORK_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByWorkIDIsInShouldWork() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where workID in DEFAULT_WORK_ID or UPDATED_WORK_ID
        defaultDayWorksShouldBeFound("workID.in=" + DEFAULT_WORK_ID + "," + UPDATED_WORK_ID);

        // Get all the dayWorksList where workID equals to UPDATED_WORK_ID
        defaultDayWorksShouldNotBeFound("workID.in=" + UPDATED_WORK_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByWorkIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where workID is not null
        defaultDayWorksShouldBeFound("workID.specified=true");

        // Get all the dayWorksList where workID is null
        defaultDayWorksShouldNotBeFound("workID.specified=false");
    }

    @Test
    @Transactional
    void getAllDayWorksByWorkIDContainsSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where workID contains DEFAULT_WORK_ID
        defaultDayWorksShouldBeFound("workID.contains=" + DEFAULT_WORK_ID);

        // Get all the dayWorksList where workID contains UPDATED_WORK_ID
        defaultDayWorksShouldNotBeFound("workID.contains=" + UPDATED_WORK_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByWorkIDNotContainsSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where workID does not contain DEFAULT_WORK_ID
        defaultDayWorksShouldNotBeFound("workID.doesNotContain=" + DEFAULT_WORK_ID);

        // Get all the dayWorksList where workID does not contain UPDATED_WORK_ID
        defaultDayWorksShouldBeFound("workID.doesNotContain=" + UPDATED_WORK_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByUserIDIsEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where userID equals to DEFAULT_USER_ID
        defaultDayWorksShouldBeFound("userID.equals=" + DEFAULT_USER_ID);

        // Get all the dayWorksList where userID equals to UPDATED_USER_ID
        defaultDayWorksShouldNotBeFound("userID.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByUserIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where userID not equals to DEFAULT_USER_ID
        defaultDayWorksShouldNotBeFound("userID.notEquals=" + DEFAULT_USER_ID);

        // Get all the dayWorksList where userID not equals to UPDATED_USER_ID
        defaultDayWorksShouldBeFound("userID.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByUserIDIsInShouldWork() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where userID in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultDayWorksShouldBeFound("userID.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the dayWorksList where userID equals to UPDATED_USER_ID
        defaultDayWorksShouldNotBeFound("userID.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByUserIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where userID is not null
        defaultDayWorksShouldBeFound("userID.specified=true");

        // Get all the dayWorksList where userID is null
        defaultDayWorksShouldNotBeFound("userID.specified=false");
    }

    @Test
    @Transactional
    void getAllDayWorksByUserIDContainsSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where userID contains DEFAULT_USER_ID
        defaultDayWorksShouldBeFound("userID.contains=" + DEFAULT_USER_ID);

        // Get all the dayWorksList where userID contains UPDATED_USER_ID
        defaultDayWorksShouldNotBeFound("userID.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByUserIDNotContainsSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where userID does not contain DEFAULT_USER_ID
        defaultDayWorksShouldNotBeFound("userID.doesNotContain=" + DEFAULT_USER_ID);

        // Get all the dayWorksList where userID does not contain UPDATED_USER_ID
        defaultDayWorksShouldBeFound("userID.doesNotContain=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork equals to DEFAULT_DAY_WORK
        defaultDayWorksShouldBeFound("dayWork.equals=" + DEFAULT_DAY_WORK);

        // Get all the dayWorksList where dayWork equals to UPDATED_DAY_WORK
        defaultDayWorksShouldNotBeFound("dayWork.equals=" + UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork not equals to DEFAULT_DAY_WORK
        defaultDayWorksShouldNotBeFound("dayWork.notEquals=" + DEFAULT_DAY_WORK);

        // Get all the dayWorksList where dayWork not equals to UPDATED_DAY_WORK
        defaultDayWorksShouldBeFound("dayWork.notEquals=" + UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsInShouldWork() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork in DEFAULT_DAY_WORK or UPDATED_DAY_WORK
        defaultDayWorksShouldBeFound("dayWork.in=" + DEFAULT_DAY_WORK + "," + UPDATED_DAY_WORK);

        // Get all the dayWorksList where dayWork equals to UPDATED_DAY_WORK
        defaultDayWorksShouldNotBeFound("dayWork.in=" + UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsNullOrNotNull() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork is not null
        defaultDayWorksShouldBeFound("dayWork.specified=true");

        // Get all the dayWorksList where dayWork is null
        defaultDayWorksShouldNotBeFound("dayWork.specified=false");
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork is greater than or equal to DEFAULT_DAY_WORK
        defaultDayWorksShouldBeFound("dayWork.greaterThanOrEqual=" + DEFAULT_DAY_WORK);

        // Get all the dayWorksList where dayWork is greater than or equal to UPDATED_DAY_WORK
        defaultDayWorksShouldNotBeFound("dayWork.greaterThanOrEqual=" + UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork is less than or equal to DEFAULT_DAY_WORK
        defaultDayWorksShouldBeFound("dayWork.lessThanOrEqual=" + DEFAULT_DAY_WORK);

        // Get all the dayWorksList where dayWork is less than or equal to SMALLER_DAY_WORK
        defaultDayWorksShouldNotBeFound("dayWork.lessThanOrEqual=" + SMALLER_DAY_WORK);
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsLessThanSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork is less than DEFAULT_DAY_WORK
        defaultDayWorksShouldNotBeFound("dayWork.lessThan=" + DEFAULT_DAY_WORK);

        // Get all the dayWorksList where dayWork is less than UPDATED_DAY_WORK
        defaultDayWorksShouldBeFound("dayWork.lessThan=" + UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void getAllDayWorksByDayWorkIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        // Get all the dayWorksList where dayWork is greater than DEFAULT_DAY_WORK
        defaultDayWorksShouldNotBeFound("dayWork.greaterThan=" + DEFAULT_DAY_WORK);

        // Get all the dayWorksList where dayWork is greater than SMALLER_DAY_WORK
        defaultDayWorksShouldBeFound("dayWork.greaterThan=" + SMALLER_DAY_WORK);
    }

    @Test
    @Transactional
    void getAllDayWorksByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);
        Member member = MemberResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        dayWorks.setMember(member);
        dayWorksRepository.saveAndFlush(dayWorks);
        Long memberId = member.getId();

        // Get all the dayWorksList where member equals to memberId
        defaultDayWorksShouldBeFound("memberId.equals=" + memberId);

        // Get all the dayWorksList where member equals to (memberId + 1)
        defaultDayWorksShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDayWorksShouldBeFound(String filter) throws Exception {
        restDayWorksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dayWorks.getId().intValue())))
            .andExpect(jsonPath("$.[*].workID").value(hasItem(DEFAULT_WORK_ID)))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].dayWork").value(hasItem(DEFAULT_DAY_WORK)));

        // Check, that the count call also returns 1
        restDayWorksMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDayWorksShouldNotBeFound(String filter) throws Exception {
        restDayWorksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDayWorksMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDayWorks() throws Exception {
        // Get the dayWorks
        restDayWorksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDayWorks() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();

        // Update the dayWorks
        DayWorks updatedDayWorks = dayWorksRepository.findById(dayWorks.getId()).get();
        // Disconnect from session so that the updates on updatedDayWorks are not directly saved in db
        em.detach(updatedDayWorks);
        updatedDayWorks.workID(UPDATED_WORK_ID).userID(UPDATED_USER_ID).dayWork(UPDATED_DAY_WORK);
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(updatedDayWorks);

        restDayWorksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dayWorksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayWorksDTO))
            )
            .andExpect(status().isOk());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
        DayWorks testDayWorks = dayWorksList.get(dayWorksList.size() - 1);
        assertThat(testDayWorks.getWorkID()).isEqualTo(UPDATED_WORK_ID);
        assertThat(testDayWorks.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testDayWorks.getDayWork()).isEqualTo(UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void putNonExistingDayWorks() throws Exception {
        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();
        dayWorks.setId(count.incrementAndGet());

        // Create the DayWorks
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayWorksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dayWorksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayWorksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDayWorks() throws Exception {
        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();
        dayWorks.setId(count.incrementAndGet());

        // Create the DayWorks
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayWorksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayWorksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDayWorks() throws Exception {
        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();
        dayWorks.setId(count.incrementAndGet());

        // Create the DayWorks
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayWorksMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayWorksDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDayWorksWithPatch() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();

        // Update the dayWorks using partial update
        DayWorks partialUpdatedDayWorks = new DayWorks();
        partialUpdatedDayWorks.setId(dayWorks.getId());

        partialUpdatedDayWorks.workID(UPDATED_WORK_ID).dayWork(UPDATED_DAY_WORK);

        restDayWorksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDayWorks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDayWorks))
            )
            .andExpect(status().isOk());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
        DayWorks testDayWorks = dayWorksList.get(dayWorksList.size() - 1);
        assertThat(testDayWorks.getWorkID()).isEqualTo(UPDATED_WORK_ID);
        assertThat(testDayWorks.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testDayWorks.getDayWork()).isEqualTo(UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void fullUpdateDayWorksWithPatch() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();

        // Update the dayWorks using partial update
        DayWorks partialUpdatedDayWorks = new DayWorks();
        partialUpdatedDayWorks.setId(dayWorks.getId());

        partialUpdatedDayWorks.workID(UPDATED_WORK_ID).userID(UPDATED_USER_ID).dayWork(UPDATED_DAY_WORK);

        restDayWorksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDayWorks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDayWorks))
            )
            .andExpect(status().isOk());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
        DayWorks testDayWorks = dayWorksList.get(dayWorksList.size() - 1);
        assertThat(testDayWorks.getWorkID()).isEqualTo(UPDATED_WORK_ID);
        assertThat(testDayWorks.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testDayWorks.getDayWork()).isEqualTo(UPDATED_DAY_WORK);
    }

    @Test
    @Transactional
    void patchNonExistingDayWorks() throws Exception {
        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();
        dayWorks.setId(count.incrementAndGet());

        // Create the DayWorks
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayWorksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dayWorksDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dayWorksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDayWorks() throws Exception {
        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();
        dayWorks.setId(count.incrementAndGet());

        // Create the DayWorks
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayWorksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dayWorksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDayWorks() throws Exception {
        int databaseSizeBeforeUpdate = dayWorksRepository.findAll().size();
        dayWorks.setId(count.incrementAndGet());

        // Create the DayWorks
        DayWorksDTO dayWorksDTO = dayWorksMapper.toDto(dayWorks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayWorksMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dayWorksDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DayWorks in the database
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDayWorks() throws Exception {
        // Initialize the database
        dayWorksRepository.saveAndFlush(dayWorks);

        int databaseSizeBeforeDelete = dayWorksRepository.findAll().size();

        // Delete the dayWorks
        restDayWorksMockMvc
            .perform(delete(ENTITY_API_URL_ID, dayWorks.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DayWorks> dayWorksList = dayWorksRepository.findAll();
        assertThat(dayWorksList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
