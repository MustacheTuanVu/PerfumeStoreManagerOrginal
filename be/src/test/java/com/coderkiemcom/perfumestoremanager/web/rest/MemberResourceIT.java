package com.coderkiemcom.perfumestoremanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coderkiemcom.perfumestoremanager.IntegrationTest;
import com.coderkiemcom.perfumestoremanager.domain.Bills;
import com.coderkiemcom.perfumestoremanager.domain.DayWorks;
import com.coderkiemcom.perfumestoremanager.domain.Member;
import com.coderkiemcom.perfumestoremanager.domain.Roles;
import com.coderkiemcom.perfumestoremanager.domain.Stores;
import com.coderkiemcom.perfumestoremanager.repository.MemberRepository;
import com.coderkiemcom.perfumestoremanager.service.criteria.MemberCriteria;
import com.coderkiemcom.perfumestoremanager.service.dto.MemberDTO;
import com.coderkiemcom.perfumestoremanager.service.mapper.MemberMapper;
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
 * Integration tests for the {@link MemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemberResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_ID = "AAAAAAAAAA";
    private static final String UPDATED_STORE_ID = "BBBBBBBBBB";

    private static final Float DEFAULT_SALARY = 1F;
    private static final Float UPDATED_SALARY = 2F;
    private static final Float SMALLER_SALARY = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberMockMvc;

    private Member member;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .userID(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .roleID(DEFAULT_ROLE_ID)
            .storeID(DEFAULT_STORE_ID)
            .salary(DEFAULT_SALARY);
        return member;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .userID(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .roleID(UPDATED_ROLE_ID)
            .storeID(UPDATED_STORE_ID)
            .salary(UPDATED_SALARY);
        return member;
    }

    @BeforeEach
    public void initTest() {
        member = createEntity(em);
    }

    @Test
    @Transactional
    void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();
        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);
        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testMember.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMember.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMember.getRoleID()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testMember.getStoreID()).isEqualTo(DEFAULT_STORE_ID);
        assertThat(testMember.getSalary()).isEqualTo(DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void createMemberWithExistingId() throws Exception {
        // Create the Member with an existing ID
        member.setId(1L);
        MemberDTO memberDTO = memberMapper.toDto(member);

        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].roleID").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].storeID").value(hasItem(DEFAULT_STORE_ID)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())));
    }

    @Test
    @Transactional
    void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.roleID").value(DEFAULT_ROLE_ID))
            .andExpect(jsonPath("$.storeID").value(DEFAULT_STORE_ID))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()));
    }

    @Test
    @Transactional
    void getMembersByIdFiltering() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        Long id = member.getId();

        defaultMemberShouldBeFound("id.equals=" + id);
        defaultMemberShouldNotBeFound("id.notEquals=" + id);

        defaultMemberShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMemberShouldNotBeFound("id.greaterThan=" + id);

        defaultMemberShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMemberShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMembersByUserIDIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where userID equals to DEFAULT_USER_ID
        defaultMemberShouldBeFound("userID.equals=" + DEFAULT_USER_ID);

        // Get all the memberList where userID equals to UPDATED_USER_ID
        defaultMemberShouldNotBeFound("userID.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMembersByUserIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where userID not equals to DEFAULT_USER_ID
        defaultMemberShouldNotBeFound("userID.notEquals=" + DEFAULT_USER_ID);

        // Get all the memberList where userID not equals to UPDATED_USER_ID
        defaultMemberShouldBeFound("userID.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMembersByUserIDIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where userID in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultMemberShouldBeFound("userID.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the memberList where userID equals to UPDATED_USER_ID
        defaultMemberShouldNotBeFound("userID.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMembersByUserIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where userID is not null
        defaultMemberShouldBeFound("userID.specified=true");

        // Get all the memberList where userID is null
        defaultMemberShouldNotBeFound("userID.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByUserIDContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where userID contains DEFAULT_USER_ID
        defaultMemberShouldBeFound("userID.contains=" + DEFAULT_USER_ID);

        // Get all the memberList where userID contains UPDATED_USER_ID
        defaultMemberShouldNotBeFound("userID.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMembersByUserIDNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where userID does not contain DEFAULT_USER_ID
        defaultMemberShouldNotBeFound("userID.doesNotContain=" + DEFAULT_USER_ID);

        // Get all the memberList where userID does not contain UPDATED_USER_ID
        defaultMemberShouldBeFound("userID.doesNotContain=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMembersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where name equals to DEFAULT_NAME
        defaultMemberShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the memberList where name equals to UPDATED_NAME
        defaultMemberShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMembersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where name not equals to DEFAULT_NAME
        defaultMemberShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the memberList where name not equals to UPDATED_NAME
        defaultMemberShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMembersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMemberShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the memberList where name equals to UPDATED_NAME
        defaultMemberShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMembersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where name is not null
        defaultMemberShouldBeFound("name.specified=true");

        // Get all the memberList where name is null
        defaultMemberShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByNameContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where name contains DEFAULT_NAME
        defaultMemberShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the memberList where name contains UPDATED_NAME
        defaultMemberShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMembersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where name does not contain DEFAULT_NAME
        defaultMemberShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the memberList where name does not contain UPDATED_NAME
        defaultMemberShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMembersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phone equals to DEFAULT_PHONE
        defaultMemberShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the memberList where phone equals to UPDATED_PHONE
        defaultMemberShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMembersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phone not equals to DEFAULT_PHONE
        defaultMemberShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the memberList where phone not equals to UPDATED_PHONE
        defaultMemberShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMembersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultMemberShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the memberList where phone equals to UPDATED_PHONE
        defaultMemberShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMembersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phone is not null
        defaultMemberShouldBeFound("phone.specified=true");

        // Get all the memberList where phone is null
        defaultMemberShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phone contains DEFAULT_PHONE
        defaultMemberShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the memberList where phone contains UPDATED_PHONE
        defaultMemberShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMembersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phone does not contain DEFAULT_PHONE
        defaultMemberShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the memberList where phone does not contain UPDATED_PHONE
        defaultMemberShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMembersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email equals to DEFAULT_EMAIL
        defaultMemberShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the memberList where email equals to UPDATED_EMAIL
        defaultMemberShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email not equals to DEFAULT_EMAIL
        defaultMemberShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the memberList where email not equals to UPDATED_EMAIL
        defaultMemberShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultMemberShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the memberList where email equals to UPDATED_EMAIL
        defaultMemberShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email is not null
        defaultMemberShouldBeFound("email.specified=true");

        // Get all the memberList where email is null
        defaultMemberShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByEmailContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email contains DEFAULT_EMAIL
        defaultMemberShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the memberList where email contains UPDATED_EMAIL
        defaultMemberShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email does not contain DEFAULT_EMAIL
        defaultMemberShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the memberList where email does not contain UPDATED_EMAIL
        defaultMemberShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMembersByRoleIDIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where roleID equals to DEFAULT_ROLE_ID
        defaultMemberShouldBeFound("roleID.equals=" + DEFAULT_ROLE_ID);

        // Get all the memberList where roleID equals to UPDATED_ROLE_ID
        defaultMemberShouldNotBeFound("roleID.equals=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByRoleIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where roleID not equals to DEFAULT_ROLE_ID
        defaultMemberShouldNotBeFound("roleID.notEquals=" + DEFAULT_ROLE_ID);

        // Get all the memberList where roleID not equals to UPDATED_ROLE_ID
        defaultMemberShouldBeFound("roleID.notEquals=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByRoleIDIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where roleID in DEFAULT_ROLE_ID or UPDATED_ROLE_ID
        defaultMemberShouldBeFound("roleID.in=" + DEFAULT_ROLE_ID + "," + UPDATED_ROLE_ID);

        // Get all the memberList where roleID equals to UPDATED_ROLE_ID
        defaultMemberShouldNotBeFound("roleID.in=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByRoleIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where roleID is not null
        defaultMemberShouldBeFound("roleID.specified=true");

        // Get all the memberList where roleID is null
        defaultMemberShouldNotBeFound("roleID.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByRoleIDContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where roleID contains DEFAULT_ROLE_ID
        defaultMemberShouldBeFound("roleID.contains=" + DEFAULT_ROLE_ID);

        // Get all the memberList where roleID contains UPDATED_ROLE_ID
        defaultMemberShouldNotBeFound("roleID.contains=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByRoleIDNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where roleID does not contain DEFAULT_ROLE_ID
        defaultMemberShouldNotBeFound("roleID.doesNotContain=" + DEFAULT_ROLE_ID);

        // Get all the memberList where roleID does not contain UPDATED_ROLE_ID
        defaultMemberShouldBeFound("roleID.doesNotContain=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByStoreIDIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where storeID equals to DEFAULT_STORE_ID
        defaultMemberShouldBeFound("storeID.equals=" + DEFAULT_STORE_ID);

        // Get all the memberList where storeID equals to UPDATED_STORE_ID
        defaultMemberShouldNotBeFound("storeID.equals=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByStoreIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where storeID not equals to DEFAULT_STORE_ID
        defaultMemberShouldNotBeFound("storeID.notEquals=" + DEFAULT_STORE_ID);

        // Get all the memberList where storeID not equals to UPDATED_STORE_ID
        defaultMemberShouldBeFound("storeID.notEquals=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByStoreIDIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where storeID in DEFAULT_STORE_ID or UPDATED_STORE_ID
        defaultMemberShouldBeFound("storeID.in=" + DEFAULT_STORE_ID + "," + UPDATED_STORE_ID);

        // Get all the memberList where storeID equals to UPDATED_STORE_ID
        defaultMemberShouldNotBeFound("storeID.in=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByStoreIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where storeID is not null
        defaultMemberShouldBeFound("storeID.specified=true");

        // Get all the memberList where storeID is null
        defaultMemberShouldNotBeFound("storeID.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByStoreIDContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where storeID contains DEFAULT_STORE_ID
        defaultMemberShouldBeFound("storeID.contains=" + DEFAULT_STORE_ID);

        // Get all the memberList where storeID contains UPDATED_STORE_ID
        defaultMemberShouldNotBeFound("storeID.contains=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllMembersByStoreIDNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where storeID does not contain DEFAULT_STORE_ID
        defaultMemberShouldNotBeFound("storeID.doesNotContain=" + DEFAULT_STORE_ID);

        // Get all the memberList where storeID does not contain UPDATED_STORE_ID
        defaultMemberShouldBeFound("storeID.doesNotContain=" + UPDATED_STORE_ID);
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary equals to DEFAULT_SALARY
        defaultMemberShouldBeFound("salary.equals=" + DEFAULT_SALARY);

        // Get all the memberList where salary equals to UPDATED_SALARY
        defaultMemberShouldNotBeFound("salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary not equals to DEFAULT_SALARY
        defaultMemberShouldNotBeFound("salary.notEquals=" + DEFAULT_SALARY);

        // Get all the memberList where salary not equals to UPDATED_SALARY
        defaultMemberShouldBeFound("salary.notEquals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary in DEFAULT_SALARY or UPDATED_SALARY
        defaultMemberShouldBeFound("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY);

        // Get all the memberList where salary equals to UPDATED_SALARY
        defaultMemberShouldNotBeFound("salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary is not null
        defaultMemberShouldBeFound("salary.specified=true");

        // Get all the memberList where salary is null
        defaultMemberShouldNotBeFound("salary.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary is greater than or equal to DEFAULT_SALARY
        defaultMemberShouldBeFound("salary.greaterThanOrEqual=" + DEFAULT_SALARY);

        // Get all the memberList where salary is greater than or equal to UPDATED_SALARY
        defaultMemberShouldNotBeFound("salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary is less than or equal to DEFAULT_SALARY
        defaultMemberShouldBeFound("salary.lessThanOrEqual=" + DEFAULT_SALARY);

        // Get all the memberList where salary is less than or equal to SMALLER_SALARY
        defaultMemberShouldNotBeFound("salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary is less than DEFAULT_SALARY
        defaultMemberShouldNotBeFound("salary.lessThan=" + DEFAULT_SALARY);

        // Get all the memberList where salary is less than UPDATED_SALARY
        defaultMemberShouldBeFound("salary.lessThan=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllMembersBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where salary is greater than DEFAULT_SALARY
        defaultMemberShouldNotBeFound("salary.greaterThan=" + DEFAULT_SALARY);

        // Get all the memberList where salary is greater than SMALLER_SALARY
        defaultMemberShouldBeFound("salary.greaterThan=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllMembersByBillsIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        Bills bills = BillsResourceIT.createEntity(em);
        em.persist(bills);
        em.flush();
        member.addBills(bills);
        memberRepository.saveAndFlush(member);
        Long billsId = bills.getId();

        // Get all the memberList where bills equals to billsId
        defaultMemberShouldBeFound("billsId.equals=" + billsId);

        // Get all the memberList where bills equals to (billsId + 1)
        defaultMemberShouldNotBeFound("billsId.equals=" + (billsId + 1));
    }

    @Test
    @Transactional
    void getAllMembersByDayWorksIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        DayWorks dayWorks = DayWorksResourceIT.createEntity(em);
        em.persist(dayWorks);
        em.flush();
        member.addDayWorks(dayWorks);
        memberRepository.saveAndFlush(member);
        Long dayWorksId = dayWorks.getId();

        // Get all the memberList where dayWorks equals to dayWorksId
        defaultMemberShouldBeFound("dayWorksId.equals=" + dayWorksId);

        // Get all the memberList where dayWorks equals to (dayWorksId + 1)
        defaultMemberShouldNotBeFound("dayWorksId.equals=" + (dayWorksId + 1));
    }

    @Test
    @Transactional
    void getAllMembersByStoresIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        Stores stores = StoresResourceIT.createEntity(em);
        em.persist(stores);
        em.flush();
        member.setStores(stores);
        memberRepository.saveAndFlush(member);
        Long storesId = stores.getId();

        // Get all the memberList where stores equals to storesId
        defaultMemberShouldBeFound("storesId.equals=" + storesId);

        // Get all the memberList where stores equals to (storesId + 1)
        defaultMemberShouldNotBeFound("storesId.equals=" + (storesId + 1));
    }

    @Test
    @Transactional
    void getAllMembersByRolesIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        Roles roles = RolesResourceIT.createEntity(em);
        em.persist(roles);
        em.flush();
        member.setRoles(roles);
        memberRepository.saveAndFlush(member);
        Long rolesId = roles.getId();

        // Get all the memberList where roles equals to rolesId
        defaultMemberShouldBeFound("rolesId.equals=" + rolesId);

        // Get all the memberList where roles equals to (rolesId + 1)
        defaultMemberShouldNotBeFound("rolesId.equals=" + (rolesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberShouldBeFound(String filter) throws Exception {
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].roleID").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].storeID").value(hasItem(DEFAULT_STORE_ID)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())));

        // Check, that the count call also returns 1
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberShouldNotBeFound(String filter) throws Exception {
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).get();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .userID(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .roleID(UPDATED_ROLE_ID)
            .storeID(UPDATED_STORE_ID)
            .salary(UPDATED_SALARY);
        MemberDTO memberDTO = memberMapper.toDto(updatedMember);

        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberDTO))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testMember.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMember.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMember.getRoleID()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testMember.getStoreID()).isEqualTo(UPDATED_STORE_ID);
        assertThat(testMember.getSalary()).isEqualTo(UPDATED_SALARY);
    }

    @Test
    @Transactional
    void putNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember.userID(UPDATED_USER_ID).salary(UPDATED_SALARY);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testMember.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMember.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMember.getRoleID()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testMember.getStoreID()).isEqualTo(DEFAULT_STORE_ID);
        assertThat(testMember.getSalary()).isEqualTo(UPDATED_SALARY);
    }

    @Test
    @Transactional
    void fullUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .userID(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .roleID(UPDATED_ROLE_ID)
            .storeID(UPDATED_STORE_ID)
            .salary(UPDATED_SALARY);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testMember.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMember.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMember.getRoleID()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testMember.getStoreID()).isEqualTo(UPDATED_STORE_ID);
        assertThat(testMember.getSalary()).isEqualTo(UPDATED_SALARY);
    }

    @Test
    @Transactional
    void patchNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(memberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Delete the member
        restMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, member.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
