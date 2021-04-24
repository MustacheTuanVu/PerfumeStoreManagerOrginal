package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Categories;
import com.coderkiemcom.perfumestoremanager.domain.Products;
import com.coderkiemcom.perfumestoremanager.repository.CategoriesRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.CategoriesCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.CategoriesDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.CategoriesMapper;
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
 * Integration tests for the {@link CategoriesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriesResourceIT {

    private static final String DEFAULT_CATEGORY_ID = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private CategoriesMapper categoriesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriesMockMvc;

    private Categories categories;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categories createEntity(EntityManager em) {
        Categories categories = new Categories().categoryID(DEFAULT_CATEGORY_ID).categoryName(DEFAULT_CATEGORY_NAME);
        return categories;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categories createUpdatedEntity(EntityManager em) {
        Categories categories = new Categories().categoryID(UPDATED_CATEGORY_ID).categoryName(UPDATED_CATEGORY_NAME);
        return categories;
    }

    @BeforeEach
    public void initTest() {
        categories = createEntity(em);
    }

    @Test
    @Transactional
    void createCategories() throws Exception {
        int databaseSizeBeforeCreate = categoriesRepository.findAll().size();
        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);
        restCategoriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriesDTO)))
            .andExpect(status().isCreated());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeCreate + 1);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryID()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testCategories.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void createCategoriesWithExistingId() throws Exception {
        // Create the Categories with an existing ID
        categories.setId(1L);
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        int databaseSizeBeforeCreate = categoriesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categories.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryID").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get the categories
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL_ID, categories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categories.getId().intValue()))
            .andExpect(jsonPath("$.categoryID").value(DEFAULT_CATEGORY_ID))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        Long id = categories.getId();

        defaultCategoriesShouldBeFound("id.equals=" + id);
        defaultCategoriesShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriesShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryIDIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryID equals to DEFAULT_CATEGORY_ID
        defaultCategoriesShouldBeFound("categoryID.equals=" + DEFAULT_CATEGORY_ID);

        // Get all the categoriesList where categoryID equals to UPDATED_CATEGORY_ID
        defaultCategoriesShouldNotBeFound("categoryID.equals=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryID not equals to DEFAULT_CATEGORY_ID
        defaultCategoriesShouldNotBeFound("categoryID.notEquals=" + DEFAULT_CATEGORY_ID);

        // Get all the categoriesList where categoryID not equals to UPDATED_CATEGORY_ID
        defaultCategoriesShouldBeFound("categoryID.notEquals=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryIDIsInShouldWork() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryID in DEFAULT_CATEGORY_ID or UPDATED_CATEGORY_ID
        defaultCategoriesShouldBeFound("categoryID.in=" + DEFAULT_CATEGORY_ID + "," + UPDATED_CATEGORY_ID);

        // Get all the categoriesList where categoryID equals to UPDATED_CATEGORY_ID
        defaultCategoriesShouldNotBeFound("categoryID.in=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryID is not null
        defaultCategoriesShouldBeFound("categoryID.specified=true");

        // Get all the categoriesList where categoryID is null
        defaultCategoriesShouldNotBeFound("categoryID.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryIDContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryID contains DEFAULT_CATEGORY_ID
        defaultCategoriesShouldBeFound("categoryID.contains=" + DEFAULT_CATEGORY_ID);

        // Get all the categoriesList where categoryID contains UPDATED_CATEGORY_ID
        defaultCategoriesShouldNotBeFound("categoryID.contains=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryIDNotContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryID does not contain DEFAULT_CATEGORY_ID
        defaultCategoriesShouldNotBeFound("categoryID.doesNotContain=" + DEFAULT_CATEGORY_ID);

        // Get all the categoriesList where categoryID does not contain UPDATED_CATEGORY_ID
        defaultCategoriesShouldBeFound("categoryID.doesNotContain=" + UPDATED_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName equals to DEFAULT_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.equals=" + DEFAULT_CATEGORY_NAME);

        // Get all the categoriesList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName not equals to DEFAULT_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.notEquals=" + DEFAULT_CATEGORY_NAME);

        // Get all the categoriesList where categoryName not equals to UPDATED_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.notEquals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName in DEFAULT_CATEGORY_NAME or UPDATED_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME);

        // Get all the categoriesList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.in=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName is not null
        defaultCategoriesShouldBeFound("categoryName.specified=true");

        // Get all the categoriesList where categoryName is null
        defaultCategoriesShouldNotBeFound("categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName contains DEFAULT_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.contains=" + DEFAULT_CATEGORY_NAME);

        // Get all the categoriesList where categoryName contains UPDATED_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName does not contain DEFAULT_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME);

        // Get all the categoriesList where categoryName does not contain UPDATED_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);
        Products products = ProductsResourceIT.createEntity(em);
        em.persist(products);
        em.flush();
        categories.addProducts(products);
        categoriesRepository.saveAndFlush(categories);
        Long productsId = products.getId();

        // Get all the categoriesList where products equals to productsId
        defaultCategoriesShouldBeFound("productsId.equals=" + productsId);

        // Get all the categoriesList where products equals to (productsId + 1)
        defaultCategoriesShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriesShouldBeFound(String filter) throws Exception {
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categories.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryID").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));

        // Check, that the count call also returns 1
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriesShouldNotBeFound(String filter) throws Exception {
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategories() throws Exception {
        // Get the categories
        restCategoriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories
        Categories updatedCategories = categoriesRepository.findById(categories.getId()).get();
        // Disconnect from session so that the updates on updatedCategories are not directly saved in db
        em.detach(updatedCategories);
        updatedCategories.categoryID(UPDATED_CATEGORY_ID).categoryName(UPDATED_CATEGORY_NAME);
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(updatedCategories);

        restCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryID()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testCategories.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriesWithPatch() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories using partial update
        Categories partialUpdatedCategories = new Categories();
        partialUpdatedCategories.setId(categories.getId());

        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategories))
            )
            .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryID()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testCategories.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCategoriesWithPatch() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories using partial update
        Categories partialUpdatedCategories = new Categories();
        partialUpdatedCategories.setId(categories.getId());

        partialUpdatedCategories.categoryID(UPDATED_CATEGORY_ID).categoryName(UPDATED_CATEGORY_NAME);

        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategories))
            )
            .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryID()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testCategories.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categoriesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeDelete = categoriesRepository.findAll().size();

        // Delete the categories
        restCategoriesMockMvc
            .perform(delete(ENTITY_API_URL_ID, categories.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
