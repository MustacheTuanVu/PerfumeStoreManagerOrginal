package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Bills;
import com.coderkiemcom.perfumestoremanager.domain.Customers;
import com.coderkiemcom.perfumestoremanager.domain.Member;
import com.coderkiemcom.perfumestoremanager.domain.OrderDetails;
import com.coderkiemcom.perfumestoremanager.repository.BillsRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.BillsCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.BillsDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.BillsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link BillsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillsResourceIT {

    private static final String DEFAULT_BILL_ID = "AAAAAAAAAA";
    private static final String UPDATED_BILL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SALES_ID = "AAAAAAAAAA";
    private static final String UPDATED_SALES_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_DISCOUNT = 1F;
    private static final Float UPDATED_DISCOUNT = 2F;
    private static final Float SMALLER_DISCOUNT = 1F - 1F;

    private static final Float DEFAULT_VAT = 1F;
    private static final Float UPDATED_VAT = 2F;
    private static final Float SMALLER_VAT = 1F - 1F;

    private static final Float DEFAULT_PAYMENT = 1F;
    private static final Float UPDATED_PAYMENT = 2F;
    private static final Float SMALLER_PAYMENT = 1F - 1F;

    private static final Float DEFAULT_TOTAL = 1F;
    private static final Float UPDATED_TOTAL = 2F;
    private static final Float SMALLER_TOTAL = 1F - 1F;

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillsRepository billsRepository;

    @Autowired
    private BillsMapper billsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillsMockMvc;

    private Bills bills;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bills createEntity(EntityManager em) {
        Bills bills = new Bills()
            .billID(DEFAULT_BILL_ID)
            .salesID(DEFAULT_SALES_ID)
            .date(DEFAULT_DATE)
            .discount(DEFAULT_DISCOUNT)
            .vat(DEFAULT_VAT)
            .payment(DEFAULT_PAYMENT)
            .total(DEFAULT_TOTAL)
            .customerID(DEFAULT_CUSTOMER_ID)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION);
        return bills;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bills createUpdatedEntity(EntityManager em) {
        Bills bills = new Bills()
            .billID(UPDATED_BILL_ID)
            .salesID(UPDATED_SALES_ID)
            .date(UPDATED_DATE)
            .discount(UPDATED_DISCOUNT)
            .vat(UPDATED_VAT)
            .payment(UPDATED_PAYMENT)
            .total(UPDATED_TOTAL)
            .customerID(UPDATED_CUSTOMER_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);
        return bills;
    }

    @BeforeEach
    public void initTest() {
        bills = createEntity(em);
    }

    @Test
    @Transactional
    void createBills() throws Exception {
        int databaseSizeBeforeCreate = billsRepository.findAll().size();
        // Create the Bills
        BillsDTO billsDTO = billsMapper.toDto(bills);
        restBillsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billsDTO)))
            .andExpect(status().isCreated());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeCreate + 1);
        Bills testBills = billsList.get(billsList.size() - 1);
        assertThat(testBills.getBillID()).isEqualTo(DEFAULT_BILL_ID);
        assertThat(testBills.getSalesID()).isEqualTo(DEFAULT_SALES_ID);
        assertThat(testBills.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBills.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testBills.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testBills.getPayment()).isEqualTo(DEFAULT_PAYMENT);
        assertThat(testBills.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testBills.getCustomerID()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testBills.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBills.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBillsWithExistingId() throws Exception {
        // Create the Bills with an existing ID
        bills.setId(1L);
        BillsDTO billsDTO = billsMapper.toDto(bills);

        int databaseSizeBeforeCreate = billsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList
        restBillsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bills.getId().intValue())))
            .andExpect(jsonPath("$.[*].billID").value(hasItem(DEFAULT_BILL_ID)))
            .andExpect(jsonPath("$.[*].salesID").value(hasItem(DEFAULT_SALES_ID)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get the bills
        restBillsMockMvc
            .perform(get(ENTITY_API_URL_ID, bills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bills.getId().intValue()))
            .andExpect(jsonPath("$.billID").value(DEFAULT_BILL_ID))
            .andExpect(jsonPath("$.salesID").value(DEFAULT_SALES_ID))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT.doubleValue()))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.customerID").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getBillsByIdFiltering() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        Long id = bills.getId();

        defaultBillsShouldBeFound("id.equals=" + id);
        defaultBillsShouldNotBeFound("id.notEquals=" + id);

        defaultBillsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBillsShouldNotBeFound("id.greaterThan=" + id);

        defaultBillsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBillsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBillsByBillIDIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where billID equals to DEFAULT_BILL_ID
        defaultBillsShouldBeFound("billID.equals=" + DEFAULT_BILL_ID);

        // Get all the billsList where billID equals to UPDATED_BILL_ID
        defaultBillsShouldNotBeFound("billID.equals=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllBillsByBillIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where billID not equals to DEFAULT_BILL_ID
        defaultBillsShouldNotBeFound("billID.notEquals=" + DEFAULT_BILL_ID);

        // Get all the billsList where billID not equals to UPDATED_BILL_ID
        defaultBillsShouldBeFound("billID.notEquals=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllBillsByBillIDIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where billID in DEFAULT_BILL_ID or UPDATED_BILL_ID
        defaultBillsShouldBeFound("billID.in=" + DEFAULT_BILL_ID + "," + UPDATED_BILL_ID);

        // Get all the billsList where billID equals to UPDATED_BILL_ID
        defaultBillsShouldNotBeFound("billID.in=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllBillsByBillIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where billID is not null
        defaultBillsShouldBeFound("billID.specified=true");

        // Get all the billsList where billID is null
        defaultBillsShouldNotBeFound("billID.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByBillIDContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where billID contains DEFAULT_BILL_ID
        defaultBillsShouldBeFound("billID.contains=" + DEFAULT_BILL_ID);

        // Get all the billsList where billID contains UPDATED_BILL_ID
        defaultBillsShouldNotBeFound("billID.contains=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllBillsByBillIDNotContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where billID does not contain DEFAULT_BILL_ID
        defaultBillsShouldNotBeFound("billID.doesNotContain=" + DEFAULT_BILL_ID);

        // Get all the billsList where billID does not contain UPDATED_BILL_ID
        defaultBillsShouldBeFound("billID.doesNotContain=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllBillsBySalesIDIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where salesID equals to DEFAULT_SALES_ID
        defaultBillsShouldBeFound("salesID.equals=" + DEFAULT_SALES_ID);

        // Get all the billsList where salesID equals to UPDATED_SALES_ID
        defaultBillsShouldNotBeFound("salesID.equals=" + UPDATED_SALES_ID);
    }

    @Test
    @Transactional
    void getAllBillsBySalesIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where salesID not equals to DEFAULT_SALES_ID
        defaultBillsShouldNotBeFound("salesID.notEquals=" + DEFAULT_SALES_ID);

        // Get all the billsList where salesID not equals to UPDATED_SALES_ID
        defaultBillsShouldBeFound("salesID.notEquals=" + UPDATED_SALES_ID);
    }

    @Test
    @Transactional
    void getAllBillsBySalesIDIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where salesID in DEFAULT_SALES_ID or UPDATED_SALES_ID
        defaultBillsShouldBeFound("salesID.in=" + DEFAULT_SALES_ID + "," + UPDATED_SALES_ID);

        // Get all the billsList where salesID equals to UPDATED_SALES_ID
        defaultBillsShouldNotBeFound("salesID.in=" + UPDATED_SALES_ID);
    }

    @Test
    @Transactional
    void getAllBillsBySalesIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where salesID is not null
        defaultBillsShouldBeFound("salesID.specified=true");

        // Get all the billsList where salesID is null
        defaultBillsShouldNotBeFound("salesID.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsBySalesIDContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where salesID contains DEFAULT_SALES_ID
        defaultBillsShouldBeFound("salesID.contains=" + DEFAULT_SALES_ID);

        // Get all the billsList where salesID contains UPDATED_SALES_ID
        defaultBillsShouldNotBeFound("salesID.contains=" + UPDATED_SALES_ID);
    }

    @Test
    @Transactional
    void getAllBillsBySalesIDNotContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where salesID does not contain DEFAULT_SALES_ID
        defaultBillsShouldNotBeFound("salesID.doesNotContain=" + DEFAULT_SALES_ID);

        // Get all the billsList where salesID does not contain UPDATED_SALES_ID
        defaultBillsShouldBeFound("salesID.doesNotContain=" + UPDATED_SALES_ID);
    }

    @Test
    @Transactional
    void getAllBillsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date equals to DEFAULT_DATE
        defaultBillsShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the billsList where date equals to UPDATED_DATE
        defaultBillsShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBillsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date not equals to DEFAULT_DATE
        defaultBillsShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the billsList where date not equals to UPDATED_DATE
        defaultBillsShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBillsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date in DEFAULT_DATE or UPDATED_DATE
        defaultBillsShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the billsList where date equals to UPDATED_DATE
        defaultBillsShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBillsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date is not null
        defaultBillsShouldBeFound("date.specified=true");

        // Get all the billsList where date is null
        defaultBillsShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date is greater than or equal to DEFAULT_DATE
        defaultBillsShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the billsList where date is greater than or equal to UPDATED_DATE
        defaultBillsShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBillsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date is less than or equal to DEFAULT_DATE
        defaultBillsShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the billsList where date is less than or equal to SMALLER_DATE
        defaultBillsShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBillsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date is less than DEFAULT_DATE
        defaultBillsShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the billsList where date is less than UPDATED_DATE
        defaultBillsShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBillsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where date is greater than DEFAULT_DATE
        defaultBillsShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the billsList where date is greater than SMALLER_DATE
        defaultBillsShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount equals to DEFAULT_DISCOUNT
        defaultBillsShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the billsList where discount equals to UPDATED_DISCOUNT
        defaultBillsShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount not equals to DEFAULT_DISCOUNT
        defaultBillsShouldNotBeFound("discount.notEquals=" + DEFAULT_DISCOUNT);

        // Get all the billsList where discount not equals to UPDATED_DISCOUNT
        defaultBillsShouldBeFound("discount.notEquals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultBillsShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the billsList where discount equals to UPDATED_DISCOUNT
        defaultBillsShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount is not null
        defaultBillsShouldBeFound("discount.specified=true");

        // Get all the billsList where discount is null
        defaultBillsShouldNotBeFound("discount.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultBillsShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the billsList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultBillsShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultBillsShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the billsList where discount is less than or equal to SMALLER_DISCOUNT
        defaultBillsShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsLessThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount is less than DEFAULT_DISCOUNT
        defaultBillsShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the billsList where discount is less than UPDATED_DISCOUNT
        defaultBillsShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllBillsByDiscountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where discount is greater than DEFAULT_DISCOUNT
        defaultBillsShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the billsList where discount is greater than SMALLER_DISCOUNT
        defaultBillsShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllBillsByVatIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat equals to DEFAULT_VAT
        defaultBillsShouldBeFound("vat.equals=" + DEFAULT_VAT);

        // Get all the billsList where vat equals to UPDATED_VAT
        defaultBillsShouldNotBeFound("vat.equals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllBillsByVatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat not equals to DEFAULT_VAT
        defaultBillsShouldNotBeFound("vat.notEquals=" + DEFAULT_VAT);

        // Get all the billsList where vat not equals to UPDATED_VAT
        defaultBillsShouldBeFound("vat.notEquals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllBillsByVatIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat in DEFAULT_VAT or UPDATED_VAT
        defaultBillsShouldBeFound("vat.in=" + DEFAULT_VAT + "," + UPDATED_VAT);

        // Get all the billsList where vat equals to UPDATED_VAT
        defaultBillsShouldNotBeFound("vat.in=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllBillsByVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat is not null
        defaultBillsShouldBeFound("vat.specified=true");

        // Get all the billsList where vat is null
        defaultBillsShouldNotBeFound("vat.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByVatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat is greater than or equal to DEFAULT_VAT
        defaultBillsShouldBeFound("vat.greaterThanOrEqual=" + DEFAULT_VAT);

        // Get all the billsList where vat is greater than or equal to UPDATED_VAT
        defaultBillsShouldNotBeFound("vat.greaterThanOrEqual=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllBillsByVatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat is less than or equal to DEFAULT_VAT
        defaultBillsShouldBeFound("vat.lessThanOrEqual=" + DEFAULT_VAT);

        // Get all the billsList where vat is less than or equal to SMALLER_VAT
        defaultBillsShouldNotBeFound("vat.lessThanOrEqual=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllBillsByVatIsLessThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat is less than DEFAULT_VAT
        defaultBillsShouldNotBeFound("vat.lessThan=" + DEFAULT_VAT);

        // Get all the billsList where vat is less than UPDATED_VAT
        defaultBillsShouldBeFound("vat.lessThan=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllBillsByVatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where vat is greater than DEFAULT_VAT
        defaultBillsShouldNotBeFound("vat.greaterThan=" + DEFAULT_VAT);

        // Get all the billsList where vat is greater than SMALLER_VAT
        defaultBillsShouldBeFound("vat.greaterThan=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment equals to DEFAULT_PAYMENT
        defaultBillsShouldBeFound("payment.equals=" + DEFAULT_PAYMENT);

        // Get all the billsList where payment equals to UPDATED_PAYMENT
        defaultBillsShouldNotBeFound("payment.equals=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment not equals to DEFAULT_PAYMENT
        defaultBillsShouldNotBeFound("payment.notEquals=" + DEFAULT_PAYMENT);

        // Get all the billsList where payment not equals to UPDATED_PAYMENT
        defaultBillsShouldBeFound("payment.notEquals=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment in DEFAULT_PAYMENT or UPDATED_PAYMENT
        defaultBillsShouldBeFound("payment.in=" + DEFAULT_PAYMENT + "," + UPDATED_PAYMENT);

        // Get all the billsList where payment equals to UPDATED_PAYMENT
        defaultBillsShouldNotBeFound("payment.in=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment is not null
        defaultBillsShouldBeFound("payment.specified=true");

        // Get all the billsList where payment is null
        defaultBillsShouldNotBeFound("payment.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment is greater than or equal to DEFAULT_PAYMENT
        defaultBillsShouldBeFound("payment.greaterThanOrEqual=" + DEFAULT_PAYMENT);

        // Get all the billsList where payment is greater than or equal to UPDATED_PAYMENT
        defaultBillsShouldNotBeFound("payment.greaterThanOrEqual=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment is less than or equal to DEFAULT_PAYMENT
        defaultBillsShouldBeFound("payment.lessThanOrEqual=" + DEFAULT_PAYMENT);

        // Get all the billsList where payment is less than or equal to SMALLER_PAYMENT
        defaultBillsShouldNotBeFound("payment.lessThanOrEqual=" + SMALLER_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsLessThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment is less than DEFAULT_PAYMENT
        defaultBillsShouldNotBeFound("payment.lessThan=" + DEFAULT_PAYMENT);

        // Get all the billsList where payment is less than UPDATED_PAYMENT
        defaultBillsShouldBeFound("payment.lessThan=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBillsByPaymentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where payment is greater than DEFAULT_PAYMENT
        defaultBillsShouldNotBeFound("payment.greaterThan=" + DEFAULT_PAYMENT);

        // Get all the billsList where payment is greater than SMALLER_PAYMENT
        defaultBillsShouldBeFound("payment.greaterThan=" + SMALLER_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total equals to DEFAULT_TOTAL
        defaultBillsShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the billsList where total equals to UPDATED_TOTAL
        defaultBillsShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total not equals to DEFAULT_TOTAL
        defaultBillsShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the billsList where total not equals to UPDATED_TOTAL
        defaultBillsShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultBillsShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the billsList where total equals to UPDATED_TOTAL
        defaultBillsShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total is not null
        defaultBillsShouldBeFound("total.specified=true");

        // Get all the billsList where total is null
        defaultBillsShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total is greater than or equal to DEFAULT_TOTAL
        defaultBillsShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the billsList where total is greater than or equal to UPDATED_TOTAL
        defaultBillsShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total is less than or equal to DEFAULT_TOTAL
        defaultBillsShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the billsList where total is less than or equal to SMALLER_TOTAL
        defaultBillsShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total is less than DEFAULT_TOTAL
        defaultBillsShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the billsList where total is less than UPDATED_TOTAL
        defaultBillsShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where total is greater than DEFAULT_TOTAL
        defaultBillsShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the billsList where total is greater than SMALLER_TOTAL
        defaultBillsShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByCustomerIDIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where customerID equals to DEFAULT_CUSTOMER_ID
        defaultBillsShouldBeFound("customerID.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the billsList where customerID equals to UPDATED_CUSTOMER_ID
        defaultBillsShouldNotBeFound("customerID.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllBillsByCustomerIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where customerID not equals to DEFAULT_CUSTOMER_ID
        defaultBillsShouldNotBeFound("customerID.notEquals=" + DEFAULT_CUSTOMER_ID);

        // Get all the billsList where customerID not equals to UPDATED_CUSTOMER_ID
        defaultBillsShouldBeFound("customerID.notEquals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllBillsByCustomerIDIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where customerID in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultBillsShouldBeFound("customerID.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the billsList where customerID equals to UPDATED_CUSTOMER_ID
        defaultBillsShouldNotBeFound("customerID.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllBillsByCustomerIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where customerID is not null
        defaultBillsShouldBeFound("customerID.specified=true");

        // Get all the billsList where customerID is null
        defaultBillsShouldNotBeFound("customerID.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByCustomerIDContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where customerID contains DEFAULT_CUSTOMER_ID
        defaultBillsShouldBeFound("customerID.contains=" + DEFAULT_CUSTOMER_ID);

        // Get all the billsList where customerID contains UPDATED_CUSTOMER_ID
        defaultBillsShouldNotBeFound("customerID.contains=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllBillsByCustomerIDNotContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where customerID does not contain DEFAULT_CUSTOMER_ID
        defaultBillsShouldNotBeFound("customerID.doesNotContain=" + DEFAULT_CUSTOMER_ID);

        // Get all the billsList where customerID does not contain UPDATED_CUSTOMER_ID
        defaultBillsShouldBeFound("customerID.doesNotContain=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status equals to DEFAULT_STATUS
        defaultBillsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the billsList where status equals to UPDATED_STATUS
        defaultBillsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status not equals to DEFAULT_STATUS
        defaultBillsShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the billsList where status not equals to UPDATED_STATUS
        defaultBillsShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBillsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the billsList where status equals to UPDATED_STATUS
        defaultBillsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status is not null
        defaultBillsShouldBeFound("status.specified=true");

        // Get all the billsList where status is null
        defaultBillsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status is greater than or equal to DEFAULT_STATUS
        defaultBillsShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the billsList where status is greater than or equal to UPDATED_STATUS
        defaultBillsShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status is less than or equal to DEFAULT_STATUS
        defaultBillsShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the billsList where status is less than or equal to SMALLER_STATUS
        defaultBillsShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status is less than DEFAULT_STATUS
        defaultBillsShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the billsList where status is less than UPDATED_STATUS
        defaultBillsShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where status is greater than DEFAULT_STATUS
        defaultBillsShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the billsList where status is greater than SMALLER_STATUS
        defaultBillsShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where description equals to DEFAULT_DESCRIPTION
        defaultBillsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the billsList where description equals to UPDATED_DESCRIPTION
        defaultBillsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBillsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where description not equals to DEFAULT_DESCRIPTION
        defaultBillsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the billsList where description not equals to UPDATED_DESCRIPTION
        defaultBillsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBillsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBillsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the billsList where description equals to UPDATED_DESCRIPTION
        defaultBillsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBillsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where description is not null
        defaultBillsShouldBeFound("description.specified=true");

        // Get all the billsList where description is null
        defaultBillsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where description contains DEFAULT_DESCRIPTION
        defaultBillsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the billsList where description contains UPDATED_DESCRIPTION
        defaultBillsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBillsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList where description does not contain DEFAULT_DESCRIPTION
        defaultBillsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the billsList where description does not contain UPDATED_DESCRIPTION
        defaultBillsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBillsByOrderDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);
        OrderDetails orderDetails = OrderDetailsResourceIT.createEntity(em);
        em.persist(orderDetails);
        em.flush();
        bills.addOrderDetails(orderDetails);
        billsRepository.saveAndFlush(bills);
        Long orderDetailsId = orderDetails.getId();

        // Get all the billsList where orderDetails equals to orderDetailsId
        defaultBillsShouldBeFound("orderDetailsId.equals=" + orderDetailsId);

        // Get all the billsList where orderDetails equals to (orderDetailsId + 1)
        defaultBillsShouldNotBeFound("orderDetailsId.equals=" + (orderDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllBillsByCustomersIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);
        Customers customers = CustomersResourceIT.createEntity(em);
        em.persist(customers);
        em.flush();
        bills.setCustomers(customers);
        billsRepository.saveAndFlush(bills);
        Long customersId = customers.getId();

        // Get all the billsList where customers equals to customersId
        defaultBillsShouldBeFound("customersId.equals=" + customersId);

        // Get all the billsList where customers equals to (customersId + 1)
        defaultBillsShouldNotBeFound("customersId.equals=" + (customersId + 1));
    }

    @Test
    @Transactional
    void getAllBillsByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);
        Member member = MemberResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        bills.setMember(member);
        billsRepository.saveAndFlush(bills);
        Long memberId = member.getId();

        // Get all the billsList where member equals to memberId
        defaultBillsShouldBeFound("memberId.equals=" + memberId);

        // Get all the billsList where member equals to (memberId + 1)
        defaultBillsShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBillsShouldBeFound(String filter) throws Exception {
        restBillsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bills.getId().intValue())))
            .andExpect(jsonPath("$.[*].billID").value(hasItem(DEFAULT_BILL_ID)))
            .andExpect(jsonPath("$.[*].salesID").value(hasItem(DEFAULT_SALES_ID)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restBillsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBillsShouldNotBeFound(String filter) throws Exception {
        restBillsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBillsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBills() throws Exception {
        // Get the bills
        restBillsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        int databaseSizeBeforeUpdate = billsRepository.findAll().size();

        // Update the bills
        Bills updatedBills = billsRepository.findById(bills.getId()).get();
        // Disconnect from session so that the updates on updatedBills are not directly saved in db
        em.detach(updatedBills);
        updatedBills
            .billID(UPDATED_BILL_ID)
            .salesID(UPDATED_SALES_ID)
            .date(UPDATED_DATE)
            .discount(UPDATED_DISCOUNT)
            .vat(UPDATED_VAT)
            .payment(UPDATED_PAYMENT)
            .total(UPDATED_TOTAL)
            .customerID(UPDATED_CUSTOMER_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);
        BillsDTO billsDTO = billsMapper.toDto(updatedBills);

        restBillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
        Bills testBills = billsList.get(billsList.size() - 1);
        assertThat(testBills.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testBills.getSalesID()).isEqualTo(UPDATED_SALES_ID);
        assertThat(testBills.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBills.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testBills.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testBills.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testBills.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testBills.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testBills.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBills.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBills() throws Exception {
        int databaseSizeBeforeUpdate = billsRepository.findAll().size();
        bills.setId(count.incrementAndGet());

        // Create the Bills
        BillsDTO billsDTO = billsMapper.toDto(bills);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBills() throws Exception {
        int databaseSizeBeforeUpdate = billsRepository.findAll().size();
        bills.setId(count.incrementAndGet());

        // Create the Bills
        BillsDTO billsDTO = billsMapper.toDto(bills);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBills() throws Exception {
        int databaseSizeBeforeUpdate = billsRepository.findAll().size();
        bills.setId(count.incrementAndGet());

        // Create the Bills
        BillsDTO billsDTO = billsMapper.toDto(bills);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillsWithPatch() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        int databaseSizeBeforeUpdate = billsRepository.findAll().size();

        // Update the bills using partial update
        Bills partialUpdatedBills = new Bills();
        partialUpdatedBills.setId(bills.getId());

        partialUpdatedBills.billID(UPDATED_BILL_ID).date(UPDATED_DATE).payment(UPDATED_PAYMENT).status(UPDATED_STATUS);

        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBills.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBills))
            )
            .andExpect(status().isOk());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
        Bills testBills = billsList.get(billsList.size() - 1);
        assertThat(testBills.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testBills.getSalesID()).isEqualTo(DEFAULT_SALES_ID);
        assertThat(testBills.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBills.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testBills.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testBills.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testBills.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testBills.getCustomerID()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testBills.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBills.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBillsWithPatch() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        int databaseSizeBeforeUpdate = billsRepository.findAll().size();

        // Update the bills using partial update
        Bills partialUpdatedBills = new Bills();
        partialUpdatedBills.setId(bills.getId());

        partialUpdatedBills
            .billID(UPDATED_BILL_ID)
            .salesID(UPDATED_SALES_ID)
            .date(UPDATED_DATE)
            .discount(UPDATED_DISCOUNT)
            .vat(UPDATED_VAT)
            .payment(UPDATED_PAYMENT)
            .total(UPDATED_TOTAL)
            .customerID(UPDATED_CUSTOMER_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);

        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBills.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBills))
            )
            .andExpect(status().isOk());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
        Bills testBills = billsList.get(billsList.size() - 1);
        assertThat(testBills.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testBills.getSalesID()).isEqualTo(UPDATED_SALES_ID);
        assertThat(testBills.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBills.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testBills.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testBills.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testBills.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testBills.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testBills.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBills.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBills() throws Exception {
        int databaseSizeBeforeUpdate = billsRepository.findAll().size();
        bills.setId(count.incrementAndGet());

        // Create the Bills
        BillsDTO billsDTO = billsMapper.toDto(bills);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBills() throws Exception {
        int databaseSizeBeforeUpdate = billsRepository.findAll().size();
        bills.setId(count.incrementAndGet());

        // Create the Bills
        BillsDTO billsDTO = billsMapper.toDto(bills);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBills() throws Exception {
        int databaseSizeBeforeUpdate = billsRepository.findAll().size();
        bills.setId(count.incrementAndGet());

        // Create the Bills
        BillsDTO billsDTO = billsMapper.toDto(bills);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(billsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        int databaseSizeBeforeDelete = billsRepository.findAll().size();

        // Delete the bills
        restBillsMockMvc
            .perform(delete(ENTITY_API_URL_ID, bills.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
