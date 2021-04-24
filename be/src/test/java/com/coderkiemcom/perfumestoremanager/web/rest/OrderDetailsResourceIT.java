package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Bills;
import com.coderkiemcom.perfumestoremanager.domain.OrderDetails;
import com.coderkiemcom.perfumestoremanager.domain.Products;
import com.coderkiemcom.perfumestoremanager.repository.OrderDetailsRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.OrderDetailsCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.OrderDetailsDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.OrderDetailsMapper;
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
 * Integration tests for the {@link OrderDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderDetailsResourceIT {

    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_ID = "AAAAAAAAAA";
    private static final String UPDATED_BILL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/order-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderDetailsMockMvc;

    private OrderDetails orderDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderDetails createEntity(EntityManager em) {
        OrderDetails orderDetails = new OrderDetails()
            .orderID(DEFAULT_ORDER_ID)
            .billID(DEFAULT_BILL_ID)
            .productID(DEFAULT_PRODUCT_ID)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE);
        return orderDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderDetails createUpdatedEntity(EntityManager em) {
        OrderDetails orderDetails = new OrderDetails()
            .orderID(UPDATED_ORDER_ID)
            .billID(UPDATED_BILL_ID)
            .productID(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE);
        return orderDetails;
    }

    @BeforeEach
    public void initTest() {
        orderDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = orderDetailsRepository.findAll().size();
        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);
        restOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderID()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrderDetails.getBillID()).isEqualTo(DEFAULT_BILL_ID);
        assertThat(testOrderDetails.getProductID()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testOrderDetails.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderDetails.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createOrderDetailsWithExistingId() throws Exception {
        // Create the OrderDetails with an existing ID
        orderDetails.setId(1L);
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);

        int databaseSizeBeforeCreate = orderDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderID").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].billID").value(hasItem(DEFAULT_BILL_ID)))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get the orderDetails
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, orderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderDetails.getId().intValue()))
            .andExpect(jsonPath("$.orderID").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.billID").value(DEFAULT_BILL_ID))
            .andExpect(jsonPath("$.productID").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getOrderDetailsByIdFiltering() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        Long id = orderDetails.getId();

        defaultOrderDetailsShouldBeFound("id.equals=" + id);
        defaultOrderDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultOrderDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderIDIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderID equals to DEFAULT_ORDER_ID
        defaultOrderDetailsShouldBeFound("orderID.equals=" + DEFAULT_ORDER_ID);

        // Get all the orderDetailsList where orderID equals to UPDATED_ORDER_ID
        defaultOrderDetailsShouldNotBeFound("orderID.equals=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderID not equals to DEFAULT_ORDER_ID
        defaultOrderDetailsShouldNotBeFound("orderID.notEquals=" + DEFAULT_ORDER_ID);

        // Get all the orderDetailsList where orderID not equals to UPDATED_ORDER_ID
        defaultOrderDetailsShouldBeFound("orderID.notEquals=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderIDIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderID in DEFAULT_ORDER_ID or UPDATED_ORDER_ID
        defaultOrderDetailsShouldBeFound("orderID.in=" + DEFAULT_ORDER_ID + "," + UPDATED_ORDER_ID);

        // Get all the orderDetailsList where orderID equals to UPDATED_ORDER_ID
        defaultOrderDetailsShouldNotBeFound("orderID.in=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderID is not null
        defaultOrderDetailsShouldBeFound("orderID.specified=true");

        // Get all the orderDetailsList where orderID is null
        defaultOrderDetailsShouldNotBeFound("orderID.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderIDContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderID contains DEFAULT_ORDER_ID
        defaultOrderDetailsShouldBeFound("orderID.contains=" + DEFAULT_ORDER_ID);

        // Get all the orderDetailsList where orderID contains UPDATED_ORDER_ID
        defaultOrderDetailsShouldNotBeFound("orderID.contains=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderIDNotContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderID does not contain DEFAULT_ORDER_ID
        defaultOrderDetailsShouldNotBeFound("orderID.doesNotContain=" + DEFAULT_ORDER_ID);

        // Get all the orderDetailsList where orderID does not contain UPDATED_ORDER_ID
        defaultOrderDetailsShouldBeFound("orderID.doesNotContain=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByBillIDIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where billID equals to DEFAULT_BILL_ID
        defaultOrderDetailsShouldBeFound("billID.equals=" + DEFAULT_BILL_ID);

        // Get all the orderDetailsList where billID equals to UPDATED_BILL_ID
        defaultOrderDetailsShouldNotBeFound("billID.equals=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByBillIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where billID not equals to DEFAULT_BILL_ID
        defaultOrderDetailsShouldNotBeFound("billID.notEquals=" + DEFAULT_BILL_ID);

        // Get all the orderDetailsList where billID not equals to UPDATED_BILL_ID
        defaultOrderDetailsShouldBeFound("billID.notEquals=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByBillIDIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where billID in DEFAULT_BILL_ID or UPDATED_BILL_ID
        defaultOrderDetailsShouldBeFound("billID.in=" + DEFAULT_BILL_ID + "," + UPDATED_BILL_ID);

        // Get all the orderDetailsList where billID equals to UPDATED_BILL_ID
        defaultOrderDetailsShouldNotBeFound("billID.in=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByBillIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where billID is not null
        defaultOrderDetailsShouldBeFound("billID.specified=true");

        // Get all the orderDetailsList where billID is null
        defaultOrderDetailsShouldNotBeFound("billID.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByBillIDContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where billID contains DEFAULT_BILL_ID
        defaultOrderDetailsShouldBeFound("billID.contains=" + DEFAULT_BILL_ID);

        // Get all the orderDetailsList where billID contains UPDATED_BILL_ID
        defaultOrderDetailsShouldNotBeFound("billID.contains=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByBillIDNotContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where billID does not contain DEFAULT_BILL_ID
        defaultOrderDetailsShouldNotBeFound("billID.doesNotContain=" + DEFAULT_BILL_ID);

        // Get all the orderDetailsList where billID does not contain UPDATED_BILL_ID
        defaultOrderDetailsShouldBeFound("billID.doesNotContain=" + UPDATED_BILL_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByProductIDIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where productID equals to DEFAULT_PRODUCT_ID
        defaultOrderDetailsShouldBeFound("productID.equals=" + DEFAULT_PRODUCT_ID);

        // Get all the orderDetailsList where productID equals to UPDATED_PRODUCT_ID
        defaultOrderDetailsShouldNotBeFound("productID.equals=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByProductIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where productID not equals to DEFAULT_PRODUCT_ID
        defaultOrderDetailsShouldNotBeFound("productID.notEquals=" + DEFAULT_PRODUCT_ID);

        // Get all the orderDetailsList where productID not equals to UPDATED_PRODUCT_ID
        defaultOrderDetailsShouldBeFound("productID.notEquals=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByProductIDIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where productID in DEFAULT_PRODUCT_ID or UPDATED_PRODUCT_ID
        defaultOrderDetailsShouldBeFound("productID.in=" + DEFAULT_PRODUCT_ID + "," + UPDATED_PRODUCT_ID);

        // Get all the orderDetailsList where productID equals to UPDATED_PRODUCT_ID
        defaultOrderDetailsShouldNotBeFound("productID.in=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByProductIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where productID is not null
        defaultOrderDetailsShouldBeFound("productID.specified=true");

        // Get all the orderDetailsList where productID is null
        defaultOrderDetailsShouldNotBeFound("productID.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByProductIDContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where productID contains DEFAULT_PRODUCT_ID
        defaultOrderDetailsShouldBeFound("productID.contains=" + DEFAULT_PRODUCT_ID);

        // Get all the orderDetailsList where productID contains UPDATED_PRODUCT_ID
        defaultOrderDetailsShouldNotBeFound("productID.contains=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByProductIDNotContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where productID does not contain DEFAULT_PRODUCT_ID
        defaultOrderDetailsShouldNotBeFound("productID.doesNotContain=" + DEFAULT_PRODUCT_ID);

        // Get all the orderDetailsList where productID does not contain UPDATED_PRODUCT_ID
        defaultOrderDetailsShouldBeFound("productID.doesNotContain=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity equals to DEFAULT_QUANTITY
        defaultOrderDetailsShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the orderDetailsList where quantity equals to UPDATED_QUANTITY
        defaultOrderDetailsShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity not equals to DEFAULT_QUANTITY
        defaultOrderDetailsShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the orderDetailsList where quantity not equals to UPDATED_QUANTITY
        defaultOrderDetailsShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultOrderDetailsShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the orderDetailsList where quantity equals to UPDATED_QUANTITY
        defaultOrderDetailsShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity is not null
        defaultOrderDetailsShouldBeFound("quantity.specified=true");

        // Get all the orderDetailsList where quantity is null
        defaultOrderDetailsShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultOrderDetailsShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderDetailsList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultOrderDetailsShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultOrderDetailsShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderDetailsList where quantity is less than or equal to SMALLER_QUANTITY
        defaultOrderDetailsShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity is less than DEFAULT_QUANTITY
        defaultOrderDetailsShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the orderDetailsList where quantity is less than UPDATED_QUANTITY
        defaultOrderDetailsShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where quantity is greater than DEFAULT_QUANTITY
        defaultOrderDetailsShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the orderDetailsList where quantity is greater than SMALLER_QUANTITY
        defaultOrderDetailsShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price equals to DEFAULT_PRICE
        defaultOrderDetailsShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the orderDetailsList where price equals to UPDATED_PRICE
        defaultOrderDetailsShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price not equals to DEFAULT_PRICE
        defaultOrderDetailsShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the orderDetailsList where price not equals to UPDATED_PRICE
        defaultOrderDetailsShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultOrderDetailsShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the orderDetailsList where price equals to UPDATED_PRICE
        defaultOrderDetailsShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price is not null
        defaultOrderDetailsShouldBeFound("price.specified=true");

        // Get all the orderDetailsList where price is null
        defaultOrderDetailsShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price is greater than or equal to DEFAULT_PRICE
        defaultOrderDetailsShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the orderDetailsList where price is greater than or equal to UPDATED_PRICE
        defaultOrderDetailsShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price is less than or equal to DEFAULT_PRICE
        defaultOrderDetailsShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the orderDetailsList where price is less than or equal to SMALLER_PRICE
        defaultOrderDetailsShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price is less than DEFAULT_PRICE
        defaultOrderDetailsShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the orderDetailsList where price is less than UPDATED_PRICE
        defaultOrderDetailsShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where price is greater than DEFAULT_PRICE
        defaultOrderDetailsShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the orderDetailsList where price is greater than SMALLER_PRICE
        defaultOrderDetailsShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByBillsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);
        Bills bills = BillsResourceIT.createEntity(em);
        em.persist(bills);
        em.flush();
        orderDetails.setBills(bills);
        orderDetailsRepository.saveAndFlush(orderDetails);
        Long billsId = bills.getId();

        // Get all the orderDetailsList where bills equals to billsId
        defaultOrderDetailsShouldBeFound("billsId.equals=" + billsId);

        // Get all the orderDetailsList where bills equals to (billsId + 1)
        defaultOrderDetailsShouldNotBeFound("billsId.equals=" + (billsId + 1));
    }

    @Test
    @Transactional
    void getAllOrderDetailsByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);
        Products products = ProductsResourceIT.createEntity(em);
        em.persist(products);
        em.flush();
        orderDetails.setProducts(products);
        orderDetailsRepository.saveAndFlush(orderDetails);
        Long productsId = products.getId();

        // Get all the orderDetailsList where products equals to productsId
        defaultOrderDetailsShouldBeFound("productsId.equals=" + productsId);

        // Get all the orderDetailsList where products equals to (productsId + 1)
        defaultOrderDetailsShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderDetailsShouldBeFound(String filter) throws Exception {
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderID").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].billID").value(hasItem(DEFAULT_BILL_ID)))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderDetailsShouldNotBeFound(String filter) throws Exception {
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderDetails() throws Exception {
        // Get the orderDetails
        restOrderDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();

        // Update the orderDetails
        OrderDetails updatedOrderDetails = orderDetailsRepository.findById(orderDetails.getId()).get();
        // Disconnect from session so that the updates on updatedOrderDetails are not directly saved in db
        em.detach(updatedOrderDetails);
        updatedOrderDetails
            .orderID(UPDATED_ORDER_ID)
            .billID(UPDATED_BILL_ID)
            .productID(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE);
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(updatedOrderDetails);

        restOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderID()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderDetails.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testOrderDetails.getProductID()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testOrderDetails.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderDetails.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();

        // Update the orderDetails using partial update
        OrderDetails partialUpdatedOrderDetails = new OrderDetails();
        partialUpdatedOrderDetails.setId(orderDetails.getId());

        partialUpdatedOrderDetails.orderID(UPDATED_ORDER_ID).quantity(UPDATED_QUANTITY);

        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderID()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderDetails.getBillID()).isEqualTo(DEFAULT_BILL_ID);
        assertThat(testOrderDetails.getProductID()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testOrderDetails.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderDetails.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();

        // Update the orderDetails using partial update
        OrderDetails partialUpdatedOrderDetails = new OrderDetails();
        partialUpdatedOrderDetails.setId(orderDetails.getId());

        partialUpdatedOrderDetails
            .orderID(UPDATED_ORDER_ID)
            .billID(UPDATED_BILL_ID)
            .productID(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE);

        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderID()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderDetails.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testOrderDetails.getProductID()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testOrderDetails.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderDetails.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.toDto(orderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeDelete = orderDetailsRepository.findAll().size();

        // Delete the orderDetails
        restOrderDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
