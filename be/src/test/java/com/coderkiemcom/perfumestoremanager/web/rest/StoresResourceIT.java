package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Member;
import com.coderkiemcom.perfumestoremanager.domain.Stores;
import com.coderkiemcom.perfumestoremanager.repository.StoresRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.StoresCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.StoresDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.StoresMapper;
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
 * Integration tests for the {@link StoresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoresResourceIT {

    private static final String DEFAULT_STORE_ID = "AAAAAAAAAA";
    private static final String UPDATED_STORE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_STORE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_STORE_ADRESS = "BBBBBBBBBB";

    private static final Float DEFAULT_STORE_RENT = 1F;
    private static final Float UPDATED_STORE_RENT = 2F;
    private static final Float SMALLER_STORE_RENT = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StoresRepository storesRepository;

    @Autowired
    private StoresMapper storesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoresMockMvc;

    private Stores stores;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stores createEntity(EntityManager em) {
        Stores stores = new Stores()
            .storeID(DEFAULT_STORE_ID)
            .storeName(DEFAULT_STORE_NAME)
            .storePhone(DEFAULT_STORE_PHONE)
            .storeAdress(DEFAULT_STORE_ADRESS)
            .storeRent(DEFAULT_STORE_RENT);
        return stores;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stores createUpdatedEntity(EntityManager em) {
        Stores stores = new Stores()
            .storeID(UPDATED_STORE_ID)
            .storeName(UPDATED_STORE_NAME)
            .storePhone(UPDATED_STORE_PHONE)
            .storeAdress(UPDATED_STORE_ADRESS)
            .storeRent(UPDATED_STORE_RENT);
        return stores;
    }

    @BeforeEach
    public void initTest() {
        stores = createEntity(em);
    }

    @Test
    @Transactional
    void createStores() throws Exception {
        int databaseSizeBeforeCreate = storesRepository.findAll().size();
        // Create the Stores
        StoresDTO storesDTO = storesMapper.toDto(stores);
        restStoresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storesDTO)))
            .andExpect(status().isCreated());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeCreate + 1);
        Stores testStores = storesList.get(storesList.size() - 1);
        assertThat(testStores.getStoreID()).isEqualTo(DEFAULT_STORE_ID);
        assertThat(testStores.getStoreName()).isEqualTo(DEFAULT_STORE_NAME);
        assertThat(testStores.getStorePhone()).isEqualTo(DEFAULT_STORE_PHONE);
        assertThat(testStores.getStoreAdress()).isEqualTo(DEFAULT_STORE_ADRESS);
        assertThat(testStores.getStoreRent()).isEqualTo(DEFAULT_STORE_RENT);
    }

    @Test
    @Transactional
    void createStoresWithExistingId() throws Exception {
        // Create the Stores with an existing ID
        stores.setId(1L);
        StoresDTO storesDTO = storesMapper.toDto(stores);

        int databaseSizeBeforeCreate = storesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStores() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList
        restStoresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stores.getId().intValue())))
            .andExpect(jsonPath("$.[*].storeID").value(hasItem(DEFAULT_STORE_ID)))
            .andExpect(jsonPath("$.[*].storeName").value(hasItem(DEFAULT_STORE_NAME)))
            .andExpect(jsonPath("$.[*].storePhone").value(hasItem(DEFAULT_STORE_PHONE)))
            .andExpect(jsonPath("$.[*].storeAdress").value(hasItem(DEFAULT_STORE_ADRESS)))
            .andExpect(jsonPath("$.[*].storeRent").value(hasItem(DEFAULT_STORE_RENT.doubleValue())));
    }

    @Test
    @Transactional
    void getStores() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get the stores
        restStoresMockMvc
            .perform(get(ENTITY_API_URL_ID, stores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stores.getId().intValue()))
            .andExpect(jsonPath("$.storeID").value(DEFAULT_STORE_ID))
            .andExpect(jsonPath("$.storeName").value(DEFAULT_STORE_NAME))
            .andExpect(jsonPath("$.storePhone").value(DEFAULT_STORE_PHONE))
            .andExpect(jsonPath("$.storeAdress").value(DEFAULT_STORE_ADRESS))
            .andExpect(jsonPath("$.storeRent").value(DEFAULT_STORE_RENT.doubleValue()));
    }

    @Test
    @Transactional
    void getStoresByIdFiltering() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        Long id = stores.getId();

        defaultStoresShouldBeFound("id.equals=" + id);
        defaultStoresShouldNotBeFound("id.notEquals=" + id);

        defaultStoresShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStoresShouldNotBeFound("id.greaterThan=" + id);

        defaultStoresShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStoresShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStoresByStoreIDIsEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeID equals to DEFAULT_STORE_ID
        defaultStoresShouldBeFound("storeID.equals=" + DEFAULT_STORE_ID);

        // Get all the storesList where storeID equals to UPDATED_STORE_ID
        defaultStoresShouldNotBeFound("storeID.equals=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllStoresByStoreIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeID not equals to DEFAULT_STORE_ID
        defaultStoresShouldNotBeFound("storeID.notEquals=" + DEFAULT_STORE_ID);

        // Get all the storesList where storeID not equals to UPDATED_STORE_ID
        defaultStoresShouldBeFound("storeID.notEquals=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllStoresByStoreIDIsInShouldWork() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeID in DEFAULT_STORE_ID or UPDATED_STORE_ID
        defaultStoresShouldBeFound("storeID.in=" + DEFAULT_STORE_ID + "," + UPDATED_STORE_ID);

        // Get all the storesList where storeID equals to UPDATED_STORE_ID
        defaultStoresShouldNotBeFound("storeID.in=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllStoresByStoreIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeID is not null
        defaultStoresShouldBeFound("storeID.specified=true");

        // Get all the storesList where storeID is null
        defaultStoresShouldNotBeFound("storeID.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByStoreIDContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeID contains DEFAULT_STORE_ID
        defaultStoresShouldBeFound("storeID.contains=" + DEFAULT_STORE_ID);

        // Get all the storesList where storeID contains UPDATED_STORE_ID
        defaultStoresShouldNotBeFound("storeID.contains=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllStoresByStoreIDNotContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeID does not contain DEFAULT_STORE_ID
        defaultStoresShouldNotBeFound("storeID.doesNotContain=" + DEFAULT_STORE_ID);

        // Get all the storesList where storeID does not contain UPDATED_STORE_ID
        defaultStoresShouldBeFound("storeID.doesNotContain=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllStoresByStoreNameIsEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeName equals to DEFAULT_STORE_NAME
        defaultStoresShouldBeFound("storeName.equals=" + DEFAULT_STORE_NAME);

        // Get all the storesList where storeName equals to UPDATED_STORE_NAME
        defaultStoresShouldNotBeFound("storeName.equals=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByStoreNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeName not equals to DEFAULT_STORE_NAME
        defaultStoresShouldNotBeFound("storeName.notEquals=" + DEFAULT_STORE_NAME);

        // Get all the storesList where storeName not equals to UPDATED_STORE_NAME
        defaultStoresShouldBeFound("storeName.notEquals=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByStoreNameIsInShouldWork() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeName in DEFAULT_STORE_NAME or UPDATED_STORE_NAME
        defaultStoresShouldBeFound("storeName.in=" + DEFAULT_STORE_NAME + "," + UPDATED_STORE_NAME);

        // Get all the storesList where storeName equals to UPDATED_STORE_NAME
        defaultStoresShouldNotBeFound("storeName.in=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByStoreNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeName is not null
        defaultStoresShouldBeFound("storeName.specified=true");

        // Get all the storesList where storeName is null
        defaultStoresShouldNotBeFound("storeName.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByStoreNameContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeName contains DEFAULT_STORE_NAME
        defaultStoresShouldBeFound("storeName.contains=" + DEFAULT_STORE_NAME);

        // Get all the storesList where storeName contains UPDATED_STORE_NAME
        defaultStoresShouldNotBeFound("storeName.contains=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByStoreNameNotContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeName does not contain DEFAULT_STORE_NAME
        defaultStoresShouldNotBeFound("storeName.doesNotContain=" + DEFAULT_STORE_NAME);

        // Get all the storesList where storeName does not contain UPDATED_STORE_NAME
        defaultStoresShouldBeFound("storeName.doesNotContain=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByStorePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storePhone equals to DEFAULT_STORE_PHONE
        defaultStoresShouldBeFound("storePhone.equals=" + DEFAULT_STORE_PHONE);

        // Get all the storesList where storePhone equals to UPDATED_STORE_PHONE
        defaultStoresShouldNotBeFound("storePhone.equals=" + UPDATED_STORE_PHONE);
    }

    @Test
    @Transactional
    void getAllStoresByStorePhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storePhone not equals to DEFAULT_STORE_PHONE
        defaultStoresShouldNotBeFound("storePhone.notEquals=" + DEFAULT_STORE_PHONE);

        // Get all the storesList where storePhone not equals to UPDATED_STORE_PHONE
        defaultStoresShouldBeFound("storePhone.notEquals=" + UPDATED_STORE_PHONE);
    }

    @Test
    @Transactional
    void getAllStoresByStorePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storePhone in DEFAULT_STORE_PHONE or UPDATED_STORE_PHONE
        defaultStoresShouldBeFound("storePhone.in=" + DEFAULT_STORE_PHONE + "," + UPDATED_STORE_PHONE);

        // Get all the storesList where storePhone equals to UPDATED_STORE_PHONE
        defaultStoresShouldNotBeFound("storePhone.in=" + UPDATED_STORE_PHONE);
    }

    @Test
    @Transactional
    void getAllStoresByStorePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storePhone is not null
        defaultStoresShouldBeFound("storePhone.specified=true");

        // Get all the storesList where storePhone is null
        defaultStoresShouldNotBeFound("storePhone.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByStorePhoneContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storePhone contains DEFAULT_STORE_PHONE
        defaultStoresShouldBeFound("storePhone.contains=" + DEFAULT_STORE_PHONE);

        // Get all the storesList where storePhone contains UPDATED_STORE_PHONE
        defaultStoresShouldNotBeFound("storePhone.contains=" + UPDATED_STORE_PHONE);
    }

    @Test
    @Transactional
    void getAllStoresByStorePhoneNotContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storePhone does not contain DEFAULT_STORE_PHONE
        defaultStoresShouldNotBeFound("storePhone.doesNotContain=" + DEFAULT_STORE_PHONE);

        // Get all the storesList where storePhone does not contain UPDATED_STORE_PHONE
        defaultStoresShouldBeFound("storePhone.doesNotContain=" + UPDATED_STORE_PHONE);
    }

    @Test
    @Transactional
    void getAllStoresByStoreAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeAdress equals to DEFAULT_STORE_ADRESS
        defaultStoresShouldBeFound("storeAdress.equals=" + DEFAULT_STORE_ADRESS);

        // Get all the storesList where storeAdress equals to UPDATED_STORE_ADRESS
        defaultStoresShouldNotBeFound("storeAdress.equals=" + UPDATED_STORE_ADRESS);
    }

    @Test
    @Transactional
    void getAllStoresByStoreAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeAdress not equals to DEFAULT_STORE_ADRESS
        defaultStoresShouldNotBeFound("storeAdress.notEquals=" + DEFAULT_STORE_ADRESS);

        // Get all the storesList where storeAdress not equals to UPDATED_STORE_ADRESS
        defaultStoresShouldBeFound("storeAdress.notEquals=" + UPDATED_STORE_ADRESS);
    }

    @Test
    @Transactional
    void getAllStoresByStoreAdressIsInShouldWork() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeAdress in DEFAULT_STORE_ADRESS or UPDATED_STORE_ADRESS
        defaultStoresShouldBeFound("storeAdress.in=" + DEFAULT_STORE_ADRESS + "," + UPDATED_STORE_ADRESS);

        // Get all the storesList where storeAdress equals to UPDATED_STORE_ADRESS
        defaultStoresShouldNotBeFound("storeAdress.in=" + UPDATED_STORE_ADRESS);
    }

    @Test
    @Transactional
    void getAllStoresByStoreAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeAdress is not null
        defaultStoresShouldBeFound("storeAdress.specified=true");

        // Get all the storesList where storeAdress is null
        defaultStoresShouldNotBeFound("storeAdress.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByStoreAdressContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeAdress contains DEFAULT_STORE_ADRESS
        defaultStoresShouldBeFound("storeAdress.contains=" + DEFAULT_STORE_ADRESS);

        // Get all the storesList where storeAdress contains UPDATED_STORE_ADRESS
        defaultStoresShouldNotBeFound("storeAdress.contains=" + UPDATED_STORE_ADRESS);
    }

    @Test
    @Transactional
    void getAllStoresByStoreAdressNotContainsSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeAdress does not contain DEFAULT_STORE_ADRESS
        defaultStoresShouldNotBeFound("storeAdress.doesNotContain=" + DEFAULT_STORE_ADRESS);

        // Get all the storesList where storeAdress does not contain UPDATED_STORE_ADRESS
        defaultStoresShouldBeFound("storeAdress.doesNotContain=" + UPDATED_STORE_ADRESS);
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent equals to DEFAULT_STORE_RENT
        defaultStoresShouldBeFound("storeRent.equals=" + DEFAULT_STORE_RENT);

        // Get all the storesList where storeRent equals to UPDATED_STORE_RENT
        defaultStoresShouldNotBeFound("storeRent.equals=" + UPDATED_STORE_RENT);
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent not equals to DEFAULT_STORE_RENT
        defaultStoresShouldNotBeFound("storeRent.notEquals=" + DEFAULT_STORE_RENT);

        // Get all the storesList where storeRent not equals to UPDATED_STORE_RENT
        defaultStoresShouldBeFound("storeRent.notEquals=" + UPDATED_STORE_RENT);
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsInShouldWork() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent in DEFAULT_STORE_RENT or UPDATED_STORE_RENT
        defaultStoresShouldBeFound("storeRent.in=" + DEFAULT_STORE_RENT + "," + UPDATED_STORE_RENT);

        // Get all the storesList where storeRent equals to UPDATED_STORE_RENT
        defaultStoresShouldNotBeFound("storeRent.in=" + UPDATED_STORE_RENT);
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsNullOrNotNull() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent is not null
        defaultStoresShouldBeFound("storeRent.specified=true");

        // Get all the storesList where storeRent is null
        defaultStoresShouldNotBeFound("storeRent.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent is greater than or equal to DEFAULT_STORE_RENT
        defaultStoresShouldBeFound("storeRent.greaterThanOrEqual=" + DEFAULT_STORE_RENT);

        // Get all the storesList where storeRent is greater than or equal to UPDATED_STORE_RENT
        defaultStoresShouldNotBeFound("storeRent.greaterThanOrEqual=" + UPDATED_STORE_RENT);
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent is less than or equal to DEFAULT_STORE_RENT
        defaultStoresShouldBeFound("storeRent.lessThanOrEqual=" + DEFAULT_STORE_RENT);

        // Get all the storesList where storeRent is less than or equal to SMALLER_STORE_RENT
        defaultStoresShouldNotBeFound("storeRent.lessThanOrEqual=" + SMALLER_STORE_RENT);
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsLessThanSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent is less than DEFAULT_STORE_RENT
        defaultStoresShouldNotBeFound("storeRent.lessThan=" + DEFAULT_STORE_RENT);

        // Get all the storesList where storeRent is less than UPDATED_STORE_RENT
        defaultStoresShouldBeFound("storeRent.lessThan=" + UPDATED_STORE_RENT);
    }

    @Test
    @Transactional
    void getAllStoresByStoreRentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        // Get all the storesList where storeRent is greater than DEFAULT_STORE_RENT
        defaultStoresShouldNotBeFound("storeRent.greaterThan=" + DEFAULT_STORE_RENT);

        // Get all the storesList where storeRent is greater than SMALLER_STORE_RENT
        defaultStoresShouldBeFound("storeRent.greaterThan=" + SMALLER_STORE_RENT);
    }

    @Test
    @Transactional
    void getAllStoresByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);
        Member member = MemberResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        stores.addMember(member);
        storesRepository.saveAndFlush(stores);
        Long memberId = member.getId();

        // Get all the storesList where member equals to memberId
        defaultStoresShouldBeFound("memberId.equals=" + memberId);

        // Get all the storesList where member equals to (memberId + 1)
        defaultStoresShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStoresShouldBeFound(String filter) throws Exception {
        restStoresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stores.getId().intValue())))
            .andExpect(jsonPath("$.[*].storeID").value(hasItem(DEFAULT_STORE_ID)))
            .andExpect(jsonPath("$.[*].storeName").value(hasItem(DEFAULT_STORE_NAME)))
            .andExpect(jsonPath("$.[*].storePhone").value(hasItem(DEFAULT_STORE_PHONE)))
            .andExpect(jsonPath("$.[*].storeAdress").value(hasItem(DEFAULT_STORE_ADRESS)))
            .andExpect(jsonPath("$.[*].storeRent").value(hasItem(DEFAULT_STORE_RENT.doubleValue())));

        // Check, that the count call also returns 1
        restStoresMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStoresShouldNotBeFound(String filter) throws Exception {
        restStoresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStoresMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStores() throws Exception {
        // Get the stores
        restStoresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStores() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        int databaseSizeBeforeUpdate = storesRepository.findAll().size();

        // Update the stores
        Stores updatedStores = storesRepository.findById(stores.getId()).get();
        // Disconnect from session so that the updates on updatedStores are not directly saved in db
        em.detach(updatedStores);
        updatedStores
            .storeID(UPDATED_STORE_ID)
            .storeName(UPDATED_STORE_NAME)
            .storePhone(UPDATED_STORE_PHONE)
            .storeAdress(UPDATED_STORE_ADRESS)
            .storeRent(UPDATED_STORE_RENT);
        StoresDTO storesDTO = storesMapper.toDto(updatedStores);

        restStoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
        Stores testStores = storesList.get(storesList.size() - 1);
        assertThat(testStores.getStoreID()).isEqualTo(UPDATED_STORE_ID);
        assertThat(testStores.getStoreName()).isEqualTo(UPDATED_STORE_NAME);
        assertThat(testStores.getStorePhone()).isEqualTo(UPDATED_STORE_PHONE);
        assertThat(testStores.getStoreAdress()).isEqualTo(UPDATED_STORE_ADRESS);
        assertThat(testStores.getStoreRent()).isEqualTo(UPDATED_STORE_RENT);
    }

    @Test
    @Transactional
    void putNonExistingStores() throws Exception {
        int databaseSizeBeforeUpdate = storesRepository.findAll().size();
        stores.setId(count.incrementAndGet());

        // Create the Stores
        StoresDTO storesDTO = storesMapper.toDto(stores);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStores() throws Exception {
        int databaseSizeBeforeUpdate = storesRepository.findAll().size();
        stores.setId(count.incrementAndGet());

        // Create the Stores
        StoresDTO storesDTO = storesMapper.toDto(stores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStores() throws Exception {
        int databaseSizeBeforeUpdate = storesRepository.findAll().size();
        stores.setId(count.incrementAndGet());

        // Create the Stores
        StoresDTO storesDTO = storesMapper.toDto(stores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoresMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStoresWithPatch() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        int databaseSizeBeforeUpdate = storesRepository.findAll().size();

        // Update the stores using partial update
        Stores partialUpdatedStores = new Stores();
        partialUpdatedStores.setId(stores.getId());

        partialUpdatedStores.storeName(UPDATED_STORE_NAME).storeAdress(UPDATED_STORE_ADRESS);

        restStoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStores.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStores))
            )
            .andExpect(status().isOk());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
        Stores testStores = storesList.get(storesList.size() - 1);
        assertThat(testStores.getStoreID()).isEqualTo(DEFAULT_STORE_ID);
        assertThat(testStores.getStoreName()).isEqualTo(UPDATED_STORE_NAME);
        assertThat(testStores.getStorePhone()).isEqualTo(DEFAULT_STORE_PHONE);
        assertThat(testStores.getStoreAdress()).isEqualTo(UPDATED_STORE_ADRESS);
        assertThat(testStores.getStoreRent()).isEqualTo(DEFAULT_STORE_RENT);
    }

    @Test
    @Transactional
    void fullUpdateStoresWithPatch() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        int databaseSizeBeforeUpdate = storesRepository.findAll().size();

        // Update the stores using partial update
        Stores partialUpdatedStores = new Stores();
        partialUpdatedStores.setId(stores.getId());

        partialUpdatedStores
            .storeID(UPDATED_STORE_ID)
            .storeName(UPDATED_STORE_NAME)
            .storePhone(UPDATED_STORE_PHONE)
            .storeAdress(UPDATED_STORE_ADRESS)
            .storeRent(UPDATED_STORE_RENT);

        restStoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStores.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStores))
            )
            .andExpect(status().isOk());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
        Stores testStores = storesList.get(storesList.size() - 1);
        assertThat(testStores.getStoreID()).isEqualTo(UPDATED_STORE_ID);
        assertThat(testStores.getStoreName()).isEqualTo(UPDATED_STORE_NAME);
        assertThat(testStores.getStorePhone()).isEqualTo(UPDATED_STORE_PHONE);
        assertThat(testStores.getStoreAdress()).isEqualTo(UPDATED_STORE_ADRESS);
        assertThat(testStores.getStoreRent()).isEqualTo(UPDATED_STORE_RENT);
    }

    @Test
    @Transactional
    void patchNonExistingStores() throws Exception {
        int databaseSizeBeforeUpdate = storesRepository.findAll().size();
        stores.setId(count.incrementAndGet());

        // Create the Stores
        StoresDTO storesDTO = storesMapper.toDto(stores);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStores() throws Exception {
        int databaseSizeBeforeUpdate = storesRepository.findAll().size();
        stores.setId(count.incrementAndGet());

        // Create the Stores
        StoresDTO storesDTO = storesMapper.toDto(stores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStores() throws Exception {
        int databaseSizeBeforeUpdate = storesRepository.findAll().size();
        stores.setId(count.incrementAndGet());

        // Create the Stores
        StoresDTO storesDTO = storesMapper.toDto(stores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoresMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(storesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stores in the database
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStores() throws Exception {
        // Initialize the database
        storesRepository.saveAndFlush(stores);

        int databaseSizeBeforeDelete = storesRepository.findAll().size();

        // Delete the stores
        restStoresMockMvc
            .perform(delete(ENTITY_API_URL_ID, stores.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stores> storesList = storesRepository.findAll();
        assertThat(storesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
