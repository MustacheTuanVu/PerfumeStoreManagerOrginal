package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Bills;
import com.coderkiemcom.perfumestoremanager.domain.Customers;
import com.coderkiemcom.perfumestoremanager.repository.CustomersRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.CustomersCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.CustomersDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.CustomersMapper;
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
 * Integration tests for the {@link CustomersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomersResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_GENDER = 1;
    private static final Integer UPDATED_GENDER = 2;
    private static final Integer SMALLER_GENDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private CustomersMapper customersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomersMockMvc;

    private Customers customers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customers createEntity(EntityManager em) {
        Customers customers = new Customers()
            .customerID(DEFAULT_CUSTOMER_ID)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .gender(DEFAULT_GENDER);
        return customers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customers createUpdatedEntity(EntityManager em) {
        Customers customers = new Customers()
            .customerID(UPDATED_CUSTOMER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER);
        return customers;
    }

    @BeforeEach
    public void initTest() {
        customers = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomers() throws Exception {
        int databaseSizeBeforeCreate = customersRepository.findAll().size();
        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);
        restCustomersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isCreated());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeCreate + 1);
        Customers testCustomers = customersList.get(customersList.size() - 1);
        assertThat(testCustomers.getCustomerID()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testCustomers.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomers.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCustomers.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomers.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void createCustomersWithExistingId() throws Exception {
        // Create the Customers with an existing ID
        customers.setId(1L);
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        int databaseSizeBeforeCreate = customersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customers.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));
    }

    @Test
    @Transactional
    void getCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get the customers
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL_ID, customers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customers.getId().intValue()))
            .andExpect(jsonPath("$.customerID").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        Long id = customers.getId();

        defaultCustomersShouldBeFound("id.equals=" + id);
        defaultCustomersShouldNotBeFound("id.notEquals=" + id);

        defaultCustomersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomersShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIDIsEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerID equals to DEFAULT_CUSTOMER_ID
        defaultCustomersShouldBeFound("customerID.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the customersList where customerID equals to UPDATED_CUSTOMER_ID
        defaultCustomersShouldNotBeFound("customerID.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerID not equals to DEFAULT_CUSTOMER_ID
        defaultCustomersShouldNotBeFound("customerID.notEquals=" + DEFAULT_CUSTOMER_ID);

        // Get all the customersList where customerID not equals to UPDATED_CUSTOMER_ID
        defaultCustomersShouldBeFound("customerID.notEquals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIDIsInShouldWork() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerID in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultCustomersShouldBeFound("customerID.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the customersList where customerID equals to UPDATED_CUSTOMER_ID
        defaultCustomersShouldNotBeFound("customerID.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerID is not null
        defaultCustomersShouldBeFound("customerID.specified=true");

        // Get all the customersList where customerID is null
        defaultCustomersShouldNotBeFound("customerID.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIDContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerID contains DEFAULT_CUSTOMER_ID
        defaultCustomersShouldBeFound("customerID.contains=" + DEFAULT_CUSTOMER_ID);

        // Get all the customersList where customerID contains UPDATED_CUSTOMER_ID
        defaultCustomersShouldNotBeFound("customerID.contains=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIDNotContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerID does not contain DEFAULT_CUSTOMER_ID
        defaultCustomersShouldNotBeFound("customerID.doesNotContain=" + DEFAULT_CUSTOMER_ID);

        // Get all the customersList where customerID does not contain UPDATED_CUSTOMER_ID
        defaultCustomersShouldBeFound("customerID.doesNotContain=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultCustomersShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customersList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomersShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerName not equals to DEFAULT_CUSTOMER_NAME
        defaultCustomersShouldNotBeFound("customerName.notEquals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customersList where customerName not equals to UPDATED_CUSTOMER_NAME
        defaultCustomersShouldBeFound("customerName.notEquals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultCustomersShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the customersList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomersShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerName is not null
        defaultCustomersShouldBeFound("customerName.specified=true");

        // Get all the customersList where customerName is null
        defaultCustomersShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultCustomersShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customersList where customerName contains UPDATED_CUSTOMER_NAME
        defaultCustomersShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultCustomersShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customersList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultCustomersShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where address equals to DEFAULT_ADDRESS
        defaultCustomersShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the customersList where address equals to UPDATED_ADDRESS
        defaultCustomersShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where address not equals to DEFAULT_ADDRESS
        defaultCustomersShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the customersList where address not equals to UPDATED_ADDRESS
        defaultCustomersShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCustomersShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the customersList where address equals to UPDATED_ADDRESS
        defaultCustomersShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where address is not null
        defaultCustomersShouldBeFound("address.specified=true");

        // Get all the customersList where address is null
        defaultCustomersShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAddressContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where address contains DEFAULT_ADDRESS
        defaultCustomersShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the customersList where address contains UPDATED_ADDRESS
        defaultCustomersShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where address does not contain DEFAULT_ADDRESS
        defaultCustomersShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the customersList where address does not contain UPDATED_ADDRESS
        defaultCustomersShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where phone equals to DEFAULT_PHONE
        defaultCustomersShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the customersList where phone equals to UPDATED_PHONE
        defaultCustomersShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where phone not equals to DEFAULT_PHONE
        defaultCustomersShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the customersList where phone not equals to UPDATED_PHONE
        defaultCustomersShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCustomersShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the customersList where phone equals to UPDATED_PHONE
        defaultCustomersShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where phone is not null
        defaultCustomersShouldBeFound("phone.specified=true");

        // Get all the customersList where phone is null
        defaultCustomersShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where phone contains DEFAULT_PHONE
        defaultCustomersShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the customersList where phone contains UPDATED_PHONE
        defaultCustomersShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where phone does not contain DEFAULT_PHONE
        defaultCustomersShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the customersList where phone does not contain UPDATED_PHONE
        defaultCustomersShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where email equals to DEFAULT_EMAIL
        defaultCustomersShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the customersList where email equals to UPDATED_EMAIL
        defaultCustomersShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where email not equals to DEFAULT_EMAIL
        defaultCustomersShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the customersList where email not equals to UPDATED_EMAIL
        defaultCustomersShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCustomersShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the customersList where email equals to UPDATED_EMAIL
        defaultCustomersShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where email is not null
        defaultCustomersShouldBeFound("email.specified=true");

        // Get all the customersList where email is null
        defaultCustomersShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByEmailContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where email contains DEFAULT_EMAIL
        defaultCustomersShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the customersList where email contains UPDATED_EMAIL
        defaultCustomersShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where email does not contain DEFAULT_EMAIL
        defaultCustomersShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the customersList where email does not contain UPDATED_EMAIL
        defaultCustomersShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender equals to DEFAULT_GENDER
        defaultCustomersShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the customersList where gender equals to UPDATED_GENDER
        defaultCustomersShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender not equals to DEFAULT_GENDER
        defaultCustomersShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the customersList where gender not equals to UPDATED_GENDER
        defaultCustomersShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultCustomersShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the customersList where gender equals to UPDATED_GENDER
        defaultCustomersShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender is not null
        defaultCustomersShouldBeFound("gender.specified=true");

        // Get all the customersList where gender is null
        defaultCustomersShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender is greater than or equal to DEFAULT_GENDER
        defaultCustomersShouldBeFound("gender.greaterThanOrEqual=" + DEFAULT_GENDER);

        // Get all the customersList where gender is greater than or equal to UPDATED_GENDER
        defaultCustomersShouldNotBeFound("gender.greaterThanOrEqual=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender is less than or equal to DEFAULT_GENDER
        defaultCustomersShouldBeFound("gender.lessThanOrEqual=" + DEFAULT_GENDER);

        // Get all the customersList where gender is less than or equal to SMALLER_GENDER
        defaultCustomersShouldNotBeFound("gender.lessThanOrEqual=" + SMALLER_GENDER);
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsLessThanSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender is less than DEFAULT_GENDER
        defaultCustomersShouldNotBeFound("gender.lessThan=" + DEFAULT_GENDER);

        // Get all the customersList where gender is less than UPDATED_GENDER
        defaultCustomersShouldBeFound("gender.lessThan=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllCustomersByGenderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList where gender is greater than DEFAULT_GENDER
        defaultCustomersShouldNotBeFound("gender.greaterThan=" + DEFAULT_GENDER);

        // Get all the customersList where gender is greater than SMALLER_GENDER
        defaultCustomersShouldBeFound("gender.greaterThan=" + SMALLER_GENDER);
    }

    @Test
    @Transactional
    void getAllCustomersByBillsIsEqualToSomething() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);
        Bills bills = BillsResourceIT.createEntity(em);
        em.persist(bills);
        em.flush();
        customers.addBills(bills);
        customersRepository.saveAndFlush(customers);
        Long billsId = bills.getId();

        // Get all the customersList where bills equals to billsId
        defaultCustomersShouldBeFound("billsId.equals=" + billsId);

        // Get all the customersList where bills equals to (billsId + 1)
        defaultCustomersShouldNotBeFound("billsId.equals=" + (billsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomersShouldBeFound(String filter) throws Exception {
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customers.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));

        // Check, that the count call also returns 1
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomersShouldNotBeFound(String filter) throws Exception {
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomers() throws Exception {
        // Get the customers
        restCustomersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        int databaseSizeBeforeUpdate = customersRepository.findAll().size();

        // Update the customers
        Customers updatedCustomers = customersRepository.findById(customers.getId()).get();
        // Disconnect from session so that the updates on updatedCustomers are not directly saved in db
        em.detach(updatedCustomers);
        updatedCustomers
            .customerID(UPDATED_CUSTOMER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER);
        CustomersDTO customersDTO = customersMapper.toDto(updatedCustomers);

        restCustomersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
        Customers testCustomers = customersList.get(customersList.size() - 1);
        assertThat(testCustomers.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCustomers.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomers.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomers.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void putNonExistingCustomers() throws Exception {
        int databaseSizeBeforeUpdate = customersRepository.findAll().size();
        customers.setId(count.incrementAndGet());

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomers() throws Exception {
        int databaseSizeBeforeUpdate = customersRepository.findAll().size();
        customers.setId(count.incrementAndGet());

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomers() throws Exception {
        int databaseSizeBeforeUpdate = customersRepository.findAll().size();
        customers.setId(count.incrementAndGet());

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomersWithPatch() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        int databaseSizeBeforeUpdate = customersRepository.findAll().size();

        // Update the customers using partial update
        Customers partialUpdatedCustomers = new Customers();
        partialUpdatedCustomers.setId(customers.getId());

        partialUpdatedCustomers
            .customerID(UPDATED_CUSTOMER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .gender(UPDATED_GENDER);

        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomers))
            )
            .andExpect(status().isOk());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
        Customers testCustomers = customersList.get(customersList.size() - 1);
        assertThat(testCustomers.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCustomers.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomers.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomers.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void fullUpdateCustomersWithPatch() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        int databaseSizeBeforeUpdate = customersRepository.findAll().size();

        // Update the customers using partial update
        Customers partialUpdatedCustomers = new Customers();
        partialUpdatedCustomers.setId(customers.getId());

        partialUpdatedCustomers
            .customerID(UPDATED_CUSTOMER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER);

        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomers))
            )
            .andExpect(status().isOk());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
        Customers testCustomers = customersList.get(customersList.size() - 1);
        assertThat(testCustomers.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCustomers.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomers.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomers.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void patchNonExistingCustomers() throws Exception {
        int databaseSizeBeforeUpdate = customersRepository.findAll().size();
        customers.setId(count.incrementAndGet());

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomers() throws Exception {
        int databaseSizeBeforeUpdate = customersRepository.findAll().size();
        customers.setId(count.incrementAndGet());

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomers() throws Exception {
        int databaseSizeBeforeUpdate = customersRepository.findAll().size();
        customers.setId(count.incrementAndGet());

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        int databaseSizeBeforeDelete = customersRepository.findAll().size();

        // Delete the customers
        restCustomersMockMvc
            .perform(delete(ENTITY_API_URL_ID, customers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
