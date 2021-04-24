package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Categories;
import com.coderkiemcom.perfumestoremanager.domain.OrderDetails;
import com.coderkiemcom.perfumestoremanager.domain.Products;
import com.coderkiemcom.perfumestoremanager.repository.ProductsRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.ProductsCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.ProductsDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.ProductsMapper;
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
 * Integration tests for the {@link ProductsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductsResourceIT {

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY_AVAILABLE = 1;
    private static final Integer UPDATED_QUANTITY_AVAILABLE = 2;
    private static final Integer SMALLER_QUANTITY_AVAILABLE = 1 - 1;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final LocalDate DEFAULT_DATE_IMPORT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_IMPORT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_IMPORT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_ID = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_ID = "BBBBBBBBBB";

    private static final Float DEFAULT_VOLUME = 1F;
    private static final Float UPDATED_VOLUME = 2F;
    private static final Float SMALLER_VOLUME = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsMockMvc;

    private Products products;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createEntity(EntityManager em) {
        Products products = new Products()
            .productID(DEFAULT_PRODUCT_ID)
            .productName(DEFAULT_PRODUCT_NAME)
            .quantityAvailable(DEFAULT_QUANTITY_AVAILABLE)
            .price(DEFAULT_PRICE)
            .dateImport(DEFAULT_DATE_IMPORT)
            .expireDate(DEFAULT_EXPIRE_DATE)
            .description(DEFAULT_DESCRIPTION)
            .categoryID(DEFAULT_CATEGORY_ID)
            .volume(DEFAULT_VOLUME);
        return products;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createUpdatedEntity(EntityManager em) {
        Products products = new Products()
            .productID(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .quantityAvailable(UPDATED_QUANTITY_AVAILABLE)
            .price(UPDATED_PRICE)
            .dateImport(UPDATED_DATE_IMPORT)
            .expireDate(UPDATED_EXPIRE_DATE)
            .description(UPDATED_DESCRIPTION)
            .categoryID(UPDATED_CATEGORY_ID)
            .volume(UPDATED_VOLUME);
        return products;
    }

    @BeforeEach
    public void initTest() {
        products = createEntity(em);
    }

    @Test
    @Transactional
    void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();
        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);
        restProductsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductID()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProducts.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProducts.getQuantityAvailable()).isEqualTo(DEFAULT_QUANTITY_AVAILABLE);
        assertThat(testProducts.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProducts.getDateImport()).isEqualTo(DEFAULT_DATE_IMPORT);
        assertThat(testProducts.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testProducts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProducts.getCategoryID()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testProducts.getVolume()).isEqualTo(DEFAULT_VOLUME);
    }

    @Test
    @Transactional
    void createProductsWithExistingId() throws Exception {
        // Create the Products with an existing ID
        products.setId(1L);
        ProductsDTO productsDTO = productsMapper.toDto(products);

        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList
        restProductsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantityAvailable").value(hasItem(DEFAULT_QUANTITY_AVAILABLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateImport").value(hasItem(DEFAULT_DATE_IMPORT.toString())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].categoryID").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.doubleValue())));
    }

    @Test
    @Transactional
    void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc
            .perform(get(ENTITY_API_URL_ID, products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.productID").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.quantityAvailable").value(DEFAULT_QUANTITY_AVAILABLE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.dateImport").value(DEFAULT_DATE_IMPORT.toString()))
            .andExpect(jsonPath("$.expireDate").value(DEFAULT_EXPIRE_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.categoryID").value(DEFAULT_CATEGORY_ID))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME.doubleValue()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        Long id = products.getId();

        defaultProductsShouldBeFound("id.equals=" + id);
        defaultProductsShouldNotBeFound("id.notEquals=" + id);

        defaultProductsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductsShouldNotBeFound("id.greaterThan=" + id);

        defaultProductsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByProductIDIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productID equals to DEFAULT_PRODUCT_ID
        defaultProductsShouldBeFound("productID.equals=" + DEFAULT_PRODUCT_ID);

        // Get all the productsList where productID equals to UPDATED_PRODUCT_ID
        defaultProductsShouldNotBeFound("productID.equals=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByProductIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productID not equals to DEFAULT_PRODUCT_ID
        defaultProductsShouldNotBeFound("productID.notEquals=" + DEFAULT_PRODUCT_ID);

        // Get all the productsList where productID not equals to UPDATED_PRODUCT_ID
        defaultProductsShouldBeFound("productID.notEquals=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByProductIDIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productID in DEFAULT_PRODUCT_ID or UPDATED_PRODUCT_ID
        defaultProductsShouldBeFound("productID.in=" + DEFAULT_PRODUCT_ID + "," + UPDATED_PRODUCT_ID);

        // Get all the productsList where productID equals to UPDATED_PRODUCT_ID
        defaultProductsShouldNotBeFound("productID.in=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByProductIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productID is not null
        defaultProductsShouldBeFound("productID.specified=true");

        // Get all the productsList where productID is null
        defaultProductsShouldNotBeFound("productID.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductIDContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productID contains DEFAULT_PRODUCT_ID
        defaultProductsShouldBeFound("productID.contains=" + DEFAULT_PRODUCT_ID);

        // Get all the productsList where productID contains UPDATED_PRODUCT_ID
        defaultProductsShouldNotBeFound("productID.contains=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByProductIDNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productID does not contain DEFAULT_PRODUCT_ID
        defaultProductsShouldNotBeFound("productID.doesNotContain=" + DEFAULT_PRODUCT_ID);

        // Get all the productsList where productID does not contain UPDATED_PRODUCT_ID
        defaultProductsShouldBeFound("productID.doesNotContain=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName equals to DEFAULT_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName not equals to DEFAULT_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.notEquals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName not equals to UPDATED_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.notEquals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the productsList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName is not null
        defaultProductsShouldBeFound("productName.specified=true");

        // Get all the productsList where productName is null
        defaultProductsShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductNameContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName contains DEFAULT_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.contains=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName contains UPDATED_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.contains=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName does not contain DEFAULT_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.doesNotContain=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName does not contain UPDATED_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.doesNotContain=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable equals to DEFAULT_QUANTITY_AVAILABLE
        defaultProductsShouldBeFound("quantityAvailable.equals=" + DEFAULT_QUANTITY_AVAILABLE);

        // Get all the productsList where quantityAvailable equals to UPDATED_QUANTITY_AVAILABLE
        defaultProductsShouldNotBeFound("quantityAvailable.equals=" + UPDATED_QUANTITY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable not equals to DEFAULT_QUANTITY_AVAILABLE
        defaultProductsShouldNotBeFound("quantityAvailable.notEquals=" + DEFAULT_QUANTITY_AVAILABLE);

        // Get all the productsList where quantityAvailable not equals to UPDATED_QUANTITY_AVAILABLE
        defaultProductsShouldBeFound("quantityAvailable.notEquals=" + UPDATED_QUANTITY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable in DEFAULT_QUANTITY_AVAILABLE or UPDATED_QUANTITY_AVAILABLE
        defaultProductsShouldBeFound("quantityAvailable.in=" + DEFAULT_QUANTITY_AVAILABLE + "," + UPDATED_QUANTITY_AVAILABLE);

        // Get all the productsList where quantityAvailable equals to UPDATED_QUANTITY_AVAILABLE
        defaultProductsShouldNotBeFound("quantityAvailable.in=" + UPDATED_QUANTITY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable is not null
        defaultProductsShouldBeFound("quantityAvailable.specified=true");

        // Get all the productsList where quantityAvailable is null
        defaultProductsShouldNotBeFound("quantityAvailable.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable is greater than or equal to DEFAULT_QUANTITY_AVAILABLE
        defaultProductsShouldBeFound("quantityAvailable.greaterThanOrEqual=" + DEFAULT_QUANTITY_AVAILABLE);

        // Get all the productsList where quantityAvailable is greater than or equal to UPDATED_QUANTITY_AVAILABLE
        defaultProductsShouldNotBeFound("quantityAvailable.greaterThanOrEqual=" + UPDATED_QUANTITY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable is less than or equal to DEFAULT_QUANTITY_AVAILABLE
        defaultProductsShouldBeFound("quantityAvailable.lessThanOrEqual=" + DEFAULT_QUANTITY_AVAILABLE);

        // Get all the productsList where quantityAvailable is less than or equal to SMALLER_QUANTITY_AVAILABLE
        defaultProductsShouldNotBeFound("quantityAvailable.lessThanOrEqual=" + SMALLER_QUANTITY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsLessThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable is less than DEFAULT_QUANTITY_AVAILABLE
        defaultProductsShouldNotBeFound("quantityAvailable.lessThan=" + DEFAULT_QUANTITY_AVAILABLE);

        // Get all the productsList where quantityAvailable is less than UPDATED_QUANTITY_AVAILABLE
        defaultProductsShouldBeFound("quantityAvailable.lessThan=" + UPDATED_QUANTITY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityAvailableIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where quantityAvailable is greater than DEFAULT_QUANTITY_AVAILABLE
        defaultProductsShouldNotBeFound("quantityAvailable.greaterThan=" + DEFAULT_QUANTITY_AVAILABLE);

        // Get all the productsList where quantityAvailable is greater than SMALLER_QUANTITY_AVAILABLE
        defaultProductsShouldBeFound("quantityAvailable.greaterThan=" + SMALLER_QUANTITY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price equals to DEFAULT_PRICE
        defaultProductsShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productsList where price equals to UPDATED_PRICE
        defaultProductsShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price not equals to DEFAULT_PRICE
        defaultProductsShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the productsList where price not equals to UPDATED_PRICE
        defaultProductsShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductsShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productsList where price equals to UPDATED_PRICE
        defaultProductsShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price is not null
        defaultProductsShouldBeFound("price.specified=true");

        // Get all the productsList where price is null
        defaultProductsShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price is greater than or equal to DEFAULT_PRICE
        defaultProductsShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productsList where price is greater than or equal to UPDATED_PRICE
        defaultProductsShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price is less than or equal to DEFAULT_PRICE
        defaultProductsShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productsList where price is less than or equal to SMALLER_PRICE
        defaultProductsShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price is less than DEFAULT_PRICE
        defaultProductsShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the productsList where price is less than UPDATED_PRICE
        defaultProductsShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where price is greater than DEFAULT_PRICE
        defaultProductsShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the productsList where price is greater than SMALLER_PRICE
        defaultProductsShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport equals to DEFAULT_DATE_IMPORT
        defaultProductsShouldBeFound("dateImport.equals=" + DEFAULT_DATE_IMPORT);

        // Get all the productsList where dateImport equals to UPDATED_DATE_IMPORT
        defaultProductsShouldNotBeFound("dateImport.equals=" + UPDATED_DATE_IMPORT);
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport not equals to DEFAULT_DATE_IMPORT
        defaultProductsShouldNotBeFound("dateImport.notEquals=" + DEFAULT_DATE_IMPORT);

        // Get all the productsList where dateImport not equals to UPDATED_DATE_IMPORT
        defaultProductsShouldBeFound("dateImport.notEquals=" + UPDATED_DATE_IMPORT);
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport in DEFAULT_DATE_IMPORT or UPDATED_DATE_IMPORT
        defaultProductsShouldBeFound("dateImport.in=" + DEFAULT_DATE_IMPORT + "," + UPDATED_DATE_IMPORT);

        // Get all the productsList where dateImport equals to UPDATED_DATE_IMPORT
        defaultProductsShouldNotBeFound("dateImport.in=" + UPDATED_DATE_IMPORT);
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport is not null
        defaultProductsShouldBeFound("dateImport.specified=true");

        // Get all the productsList where dateImport is null
        defaultProductsShouldNotBeFound("dateImport.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport is greater than or equal to DEFAULT_DATE_IMPORT
        defaultProductsShouldBeFound("dateImport.greaterThanOrEqual=" + DEFAULT_DATE_IMPORT);

        // Get all the productsList where dateImport is greater than or equal to UPDATED_DATE_IMPORT
        defaultProductsShouldNotBeFound("dateImport.greaterThanOrEqual=" + UPDATED_DATE_IMPORT);
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport is less than or equal to DEFAULT_DATE_IMPORT
        defaultProductsShouldBeFound("dateImport.lessThanOrEqual=" + DEFAULT_DATE_IMPORT);

        // Get all the productsList where dateImport is less than or equal to SMALLER_DATE_IMPORT
        defaultProductsShouldNotBeFound("dateImport.lessThanOrEqual=" + SMALLER_DATE_IMPORT);
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsLessThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport is less than DEFAULT_DATE_IMPORT
        defaultProductsShouldNotBeFound("dateImport.lessThan=" + DEFAULT_DATE_IMPORT);

        // Get all the productsList where dateImport is less than UPDATED_DATE_IMPORT
        defaultProductsShouldBeFound("dateImport.lessThan=" + UPDATED_DATE_IMPORT);
    }

    @Test
    @Transactional
    void getAllProductsByDateImportIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where dateImport is greater than DEFAULT_DATE_IMPORT
        defaultProductsShouldNotBeFound("dateImport.greaterThan=" + DEFAULT_DATE_IMPORT);

        // Get all the productsList where dateImport is greater than SMALLER_DATE_IMPORT
        defaultProductsShouldBeFound("dateImport.greaterThan=" + SMALLER_DATE_IMPORT);
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate equals to DEFAULT_EXPIRE_DATE
        defaultProductsShouldBeFound("expireDate.equals=" + DEFAULT_EXPIRE_DATE);

        // Get all the productsList where expireDate equals to UPDATED_EXPIRE_DATE
        defaultProductsShouldNotBeFound("expireDate.equals=" + UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate not equals to DEFAULT_EXPIRE_DATE
        defaultProductsShouldNotBeFound("expireDate.notEquals=" + DEFAULT_EXPIRE_DATE);

        // Get all the productsList where expireDate not equals to UPDATED_EXPIRE_DATE
        defaultProductsShouldBeFound("expireDate.notEquals=" + UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate in DEFAULT_EXPIRE_DATE or UPDATED_EXPIRE_DATE
        defaultProductsShouldBeFound("expireDate.in=" + DEFAULT_EXPIRE_DATE + "," + UPDATED_EXPIRE_DATE);

        // Get all the productsList where expireDate equals to UPDATED_EXPIRE_DATE
        defaultProductsShouldNotBeFound("expireDate.in=" + UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate is not null
        defaultProductsShouldBeFound("expireDate.specified=true");

        // Get all the productsList where expireDate is null
        defaultProductsShouldNotBeFound("expireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate is greater than or equal to DEFAULT_EXPIRE_DATE
        defaultProductsShouldBeFound("expireDate.greaterThanOrEqual=" + DEFAULT_EXPIRE_DATE);

        // Get all the productsList where expireDate is greater than or equal to UPDATED_EXPIRE_DATE
        defaultProductsShouldNotBeFound("expireDate.greaterThanOrEqual=" + UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate is less than or equal to DEFAULT_EXPIRE_DATE
        defaultProductsShouldBeFound("expireDate.lessThanOrEqual=" + DEFAULT_EXPIRE_DATE);

        // Get all the productsList where expireDate is less than or equal to SMALLER_EXPIRE_DATE
        defaultProductsShouldNotBeFound("expireDate.lessThanOrEqual=" + SMALLER_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsLessThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate is less than DEFAULT_EXPIRE_DATE
        defaultProductsShouldNotBeFound("expireDate.lessThan=" + DEFAULT_EXPIRE_DATE);

        // Get all the productsList where expireDate is less than UPDATED_EXPIRE_DATE
        defaultProductsShouldBeFound("expireDate.lessThan=" + UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpireDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where expireDate is greater than DEFAULT_EXPIRE_DATE
        defaultProductsShouldNotBeFound("expireDate.greaterThan=" + DEFAULT_EXPIRE_DATE);

        // Get all the productsList where expireDate is greater than SMALLER_EXPIRE_DATE
        defaultProductsShouldBeFound("expireDate.greaterThan=" + SMALLER_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where description equals to DEFAULT_DESCRIPTION
        defaultProductsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productsList where description equals to UPDATED_DESCRIPTION
        defaultProductsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where description not equals to DEFAULT_DESCRIPTION
        defaultProductsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productsList where description not equals to UPDATED_DESCRIPTION
        defaultProductsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productsList where description equals to UPDATED_DESCRIPTION
        defaultProductsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where description is not null
        defaultProductsShouldBeFound("description.specified=true");

        // Get all the productsList where description is null
        defaultProductsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where description contains DEFAULT_DESCRIPTION
        defaultProductsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productsList where description contains UPDATED_DESCRIPTION
        defaultProductsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where description does not contain DEFAULT_DESCRIPTION
        defaultProductsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productsList where description does not contain UPDATED_DESCRIPTION
        defaultProductsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIDIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where categoryID equals to DEFAULT_CATEGORY_ID
        defaultProductsShouldBeFound("categoryID.equals=" + DEFAULT_CATEGORY_ID);

        // Get all the productsList where categoryID equals to UPDATED_CATEGORY_ID
        defaultProductsShouldNotBeFound("categoryID.equals=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where categoryID not equals to DEFAULT_CATEGORY_ID
        defaultProductsShouldNotBeFound("categoryID.notEquals=" + DEFAULT_CATEGORY_ID);

        // Get all the productsList where categoryID not equals to UPDATED_CATEGORY_ID
        defaultProductsShouldBeFound("categoryID.notEquals=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIDIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where categoryID in DEFAULT_CATEGORY_ID or UPDATED_CATEGORY_ID
        defaultProductsShouldBeFound("categoryID.in=" + DEFAULT_CATEGORY_ID + "," + UPDATED_CATEGORY_ID);

        // Get all the productsList where categoryID equals to UPDATED_CATEGORY_ID
        defaultProductsShouldNotBeFound("categoryID.in=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where categoryID is not null
        defaultProductsShouldBeFound("categoryID.specified=true");

        // Get all the productsList where categoryID is null
        defaultProductsShouldNotBeFound("categoryID.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIDContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where categoryID contains DEFAULT_CATEGORY_ID
        defaultProductsShouldBeFound("categoryID.contains=" + DEFAULT_CATEGORY_ID);

        // Get all the productsList where categoryID contains UPDATED_CATEGORY_ID
        defaultProductsShouldNotBeFound("categoryID.contains=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIDNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where categoryID does not contain DEFAULT_CATEGORY_ID
        defaultProductsShouldNotBeFound("categoryID.doesNotContain=" + DEFAULT_CATEGORY_ID);

        // Get all the productsList where categoryID does not contain UPDATED_CATEGORY_ID
        defaultProductsShouldBeFound("categoryID.doesNotContain=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume equals to DEFAULT_VOLUME
        defaultProductsShouldBeFound("volume.equals=" + DEFAULT_VOLUME);

        // Get all the productsList where volume equals to UPDATED_VOLUME
        defaultProductsShouldNotBeFound("volume.equals=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume not equals to DEFAULT_VOLUME
        defaultProductsShouldNotBeFound("volume.notEquals=" + DEFAULT_VOLUME);

        // Get all the productsList where volume not equals to UPDATED_VOLUME
        defaultProductsShouldBeFound("volume.notEquals=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume in DEFAULT_VOLUME or UPDATED_VOLUME
        defaultProductsShouldBeFound("volume.in=" + DEFAULT_VOLUME + "," + UPDATED_VOLUME);

        // Get all the productsList where volume equals to UPDATED_VOLUME
        defaultProductsShouldNotBeFound("volume.in=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume is not null
        defaultProductsShouldBeFound("volume.specified=true");

        // Get all the productsList where volume is null
        defaultProductsShouldNotBeFound("volume.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume is greater than or equal to DEFAULT_VOLUME
        defaultProductsShouldBeFound("volume.greaterThanOrEqual=" + DEFAULT_VOLUME);

        // Get all the productsList where volume is greater than or equal to UPDATED_VOLUME
        defaultProductsShouldNotBeFound("volume.greaterThanOrEqual=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume is less than or equal to DEFAULT_VOLUME
        defaultProductsShouldBeFound("volume.lessThanOrEqual=" + DEFAULT_VOLUME);

        // Get all the productsList where volume is less than or equal to SMALLER_VOLUME
        defaultProductsShouldNotBeFound("volume.lessThanOrEqual=" + SMALLER_VOLUME);
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume is less than DEFAULT_VOLUME
        defaultProductsShouldNotBeFound("volume.lessThan=" + DEFAULT_VOLUME);

        // Get all the productsList where volume is less than UPDATED_VOLUME
        defaultProductsShouldBeFound("volume.lessThan=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void getAllProductsByVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where volume is greater than DEFAULT_VOLUME
        defaultProductsShouldNotBeFound("volume.greaterThan=" + DEFAULT_VOLUME);

        // Get all the productsList where volume is greater than SMALLER_VOLUME
        defaultProductsShouldBeFound("volume.greaterThan=" + SMALLER_VOLUME);
    }

    @Test
    @Transactional
    void getAllProductsByOrderDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        OrderDetails orderDetails = OrderDetailsResourceIT.createEntity(em);
        em.persist(orderDetails);
        em.flush();
        products.addOrderDetails(orderDetails);
        productsRepository.saveAndFlush(products);
        Long orderDetailsId = orderDetails.getId();

        // Get all the productsList where orderDetails equals to orderDetailsId
        defaultProductsShouldBeFound("orderDetailsId.equals=" + orderDetailsId);

        // Get all the productsList where orderDetails equals to (orderDetailsId + 1)
        defaultProductsShouldNotBeFound("orderDetailsId.equals=" + (orderDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCategoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        Categories categories = CategoriesResourceIT.createEntity(em);
        em.persist(categories);
        em.flush();
        products.setCategories(categories);
        productsRepository.saveAndFlush(products);
        Long categoriesId = categories.getId();

        // Get all the productsList where categories equals to categoriesId
        defaultProductsShouldBeFound("categoriesId.equals=" + categoriesId);

        // Get all the productsList where categories equals to (categoriesId + 1)
        defaultProductsShouldNotBeFound("categoriesId.equals=" + (categoriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductsShouldBeFound(String filter) throws Exception {
        restProductsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantityAvailable").value(hasItem(DEFAULT_QUANTITY_AVAILABLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateImport").value(hasItem(DEFAULT_DATE_IMPORT.toString())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].categoryID").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.doubleValue())));

        // Check, that the count call also returns 1
        restProductsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductsShouldNotBeFound(String filter) throws Exception {
        restProductsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        Products updatedProducts = productsRepository.findById(products.getId()).get();
        // Disconnect from session so that the updates on updatedProducts are not directly saved in db
        em.detach(updatedProducts);
        updatedProducts
            .productID(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .quantityAvailable(UPDATED_QUANTITY_AVAILABLE)
            .price(UPDATED_PRICE)
            .dateImport(UPDATED_DATE_IMPORT)
            .expireDate(UPDATED_EXPIRE_DATE)
            .description(UPDATED_DESCRIPTION)
            .categoryID(UPDATED_CATEGORY_ID)
            .volume(UPDATED_VOLUME);
        ProductsDTO productsDTO = productsMapper.toDto(updatedProducts);

        restProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductID()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProducts.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProducts.getQuantityAvailable()).isEqualTo(UPDATED_QUANTITY_AVAILABLE);
        assertThat(testProducts.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProducts.getDateImport()).isEqualTo(UPDATED_DATE_IMPORT);
        assertThat(testProducts.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testProducts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProducts.getCategoryID()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testProducts.getVolume()).isEqualTo(UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void putNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductsWithPatch() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products using partial update
        Products partialUpdatedProducts = new Products();
        partialUpdatedProducts.setId(products.getId());

        partialUpdatedProducts.productID(UPDATED_PRODUCT_ID).quantityAvailable(UPDATED_QUANTITY_AVAILABLE).dateImport(UPDATED_DATE_IMPORT);

        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducts))
            )
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductID()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProducts.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProducts.getQuantityAvailable()).isEqualTo(UPDATED_QUANTITY_AVAILABLE);
        assertThat(testProducts.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProducts.getDateImport()).isEqualTo(UPDATED_DATE_IMPORT);
        assertThat(testProducts.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testProducts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProducts.getCategoryID()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testProducts.getVolume()).isEqualTo(DEFAULT_VOLUME);
    }

    @Test
    @Transactional
    void fullUpdateProductsWithPatch() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products using partial update
        Products partialUpdatedProducts = new Products();
        partialUpdatedProducts.setId(products.getId());

        partialUpdatedProducts
            .productID(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .quantityAvailable(UPDATED_QUANTITY_AVAILABLE)
            .price(UPDATED_PRICE)
            .dateImport(UPDATED_DATE_IMPORT)
            .expireDate(UPDATED_EXPIRE_DATE)
            .description(UPDATED_DESCRIPTION)
            .categoryID(UPDATED_CATEGORY_ID)
            .volume(UPDATED_VOLUME);

        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducts))
            )
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductID()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProducts.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProducts.getQuantityAvailable()).isEqualTo(UPDATED_QUANTITY_AVAILABLE);
        assertThat(testProducts.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProducts.getDateImport()).isEqualTo(UPDATED_DATE_IMPORT);
        assertThat(testProducts.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testProducts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProducts.getCategoryID()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testProducts.getVolume()).isEqualTo(UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void patchNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Delete the products
        restProductsMockMvc
            .perform(delete(ENTITY_API_URL_ID, products.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
