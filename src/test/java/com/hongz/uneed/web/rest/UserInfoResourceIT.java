package com.hongz.uneed.web.rest;

import com.hongz.uneed.UneedApp;
import com.hongz.uneed.domain.UserInfo;
import com.hongz.uneed.domain.User;
import com.hongz.uneed.repository.UserInfoRepository;
import com.hongz.uneed.service.UserInfoService;
import com.hongz.uneed.web.rest.errors.ExceptionTranslator;
import com.hongz.uneed.service.dto.UserInfoCriteria;
import com.hongz.uneed.service.UserInfoQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.hongz.uneed.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hongz.uneed.domain.enumeration.Gender;
/**
 * Integration tests for the {@Link UserInfoResource} REST controller.
 */
@SpringBootTest(classes = UneedApp.class)
public class UserInfoResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMAIL_FLAG = false;
    private static final Boolean UPDATED_EMAIL_FLAG = true;

    private static final Boolean DEFAULT_SMS_FLAG = false;
    private static final Boolean UPDATED_SMS_FLAG = true;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoQueryService userInfoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserInfoResource userInfoResource = new UserInfoResource(userInfoService, userInfoQueryService);
        this.restUserInfoMockMvc = MockMvcBuilders.standaloneSetup(userInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .phone(DEFAULT_PHONE)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .emailFlag(DEFAULT_EMAIL_FLAG)
            .smsFlag(DEFAULT_SMS_FLAG)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER);
        return userInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createUpdatedEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .phone(UPDATED_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .emailFlag(UPDATED_EMAIL_FLAG)
            .smsFlag(UPDATED_SMS_FLAG)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER);
        return userInfo;
    }

    @BeforeEach
    public void initTest() {
        userInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo
        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserInfo.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testUserInfo.isEmailFlag()).isEqualTo(DEFAULT_EMAIL_FLAG);
        assertThat(testUserInfo.isSmsFlag()).isEqualTo(DEFAULT_SMS_FLAG);
        assertThat(testUserInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testUserInfo.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    public void createUserInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo with an existing ID
        userInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE.toString())))
            .andExpect(jsonPath("$.[*].emailFlag").value(hasItem(DEFAULT_EMAIL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].smsFlag").value(hasItem(DEFAULT_SMS_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }
    
    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE.toString()))
            .andExpect(jsonPath("$.emailFlag").value(DEFAULT_EMAIL_FLAG.booleanValue()))
            .andExpect(jsonPath("$.smsFlag").value(DEFAULT_SMS_FLAG.booleanValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    public void getAllUserInfosByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where phone equals to DEFAULT_PHONE
        defaultUserInfoShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the userInfoList where phone equals to UPDATED_PHONE
        defaultUserInfoShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUserInfoShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the userInfoList where phone equals to UPDATED_PHONE
        defaultUserInfoShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where phone is not null
        defaultUserInfoShouldBeFound("phone.specified=true");

        // Get all the userInfoList where phone is null
        defaultUserInfoShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByMobilePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where mobilePhone equals to DEFAULT_MOBILE_PHONE
        defaultUserInfoShouldBeFound("mobilePhone.equals=" + DEFAULT_MOBILE_PHONE);

        // Get all the userInfoList where mobilePhone equals to UPDATED_MOBILE_PHONE
        defaultUserInfoShouldNotBeFound("mobilePhone.equals=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByMobilePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where mobilePhone in DEFAULT_MOBILE_PHONE or UPDATED_MOBILE_PHONE
        defaultUserInfoShouldBeFound("mobilePhone.in=" + DEFAULT_MOBILE_PHONE + "," + UPDATED_MOBILE_PHONE);

        // Get all the userInfoList where mobilePhone equals to UPDATED_MOBILE_PHONE
        defaultUserInfoShouldNotBeFound("mobilePhone.in=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByMobilePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where mobilePhone is not null
        defaultUserInfoShouldBeFound("mobilePhone.specified=true");

        // Get all the userInfoList where mobilePhone is null
        defaultUserInfoShouldNotBeFound("mobilePhone.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailFlag equals to DEFAULT_EMAIL_FLAG
        defaultUserInfoShouldBeFound("emailFlag.equals=" + DEFAULT_EMAIL_FLAG);

        // Get all the userInfoList where emailFlag equals to UPDATED_EMAIL_FLAG
        defaultUserInfoShouldNotBeFound("emailFlag.equals=" + UPDATED_EMAIL_FLAG);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailFlagIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailFlag in DEFAULT_EMAIL_FLAG or UPDATED_EMAIL_FLAG
        defaultUserInfoShouldBeFound("emailFlag.in=" + DEFAULT_EMAIL_FLAG + "," + UPDATED_EMAIL_FLAG);

        // Get all the userInfoList where emailFlag equals to UPDATED_EMAIL_FLAG
        defaultUserInfoShouldNotBeFound("emailFlag.in=" + UPDATED_EMAIL_FLAG);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailFlag is not null
        defaultUserInfoShouldBeFound("emailFlag.specified=true");

        // Get all the userInfoList where emailFlag is null
        defaultUserInfoShouldNotBeFound("emailFlag.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosBySmsFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where smsFlag equals to DEFAULT_SMS_FLAG
        defaultUserInfoShouldBeFound("smsFlag.equals=" + DEFAULT_SMS_FLAG);

        // Get all the userInfoList where smsFlag equals to UPDATED_SMS_FLAG
        defaultUserInfoShouldNotBeFound("smsFlag.equals=" + UPDATED_SMS_FLAG);
    }

    @Test
    @Transactional
    public void getAllUserInfosBySmsFlagIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where smsFlag in DEFAULT_SMS_FLAG or UPDATED_SMS_FLAG
        defaultUserInfoShouldBeFound("smsFlag.in=" + DEFAULT_SMS_FLAG + "," + UPDATED_SMS_FLAG);

        // Get all the userInfoList where smsFlag equals to UPDATED_SMS_FLAG
        defaultUserInfoShouldNotBeFound("smsFlag.in=" + UPDATED_SMS_FLAG);
    }

    @Test
    @Transactional
    public void getAllUserInfosBySmsFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where smsFlag is not null
        defaultUserInfoShouldBeFound("smsFlag.specified=true");

        // Get all the userInfoList where smsFlag is null
        defaultUserInfoShouldNotBeFound("smsFlag.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultUserInfoShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the userInfoList where birthDate equals to UPDATED_BIRTH_DATE
        defaultUserInfoShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultUserInfoShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the userInfoList where birthDate equals to UPDATED_BIRTH_DATE
        defaultUserInfoShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate is not null
        defaultUserInfoShouldBeFound("birthDate.specified=true");

        // Get all the userInfoList where birthDate is null
        defaultUserInfoShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate greater than or equals to DEFAULT_BIRTH_DATE
        defaultUserInfoShouldBeFound("birthDate.greaterOrEqualThan=" + DEFAULT_BIRTH_DATE);

        // Get all the userInfoList where birthDate greater than or equals to UPDATED_BIRTH_DATE
        defaultUserInfoShouldNotBeFound("birthDate.greaterOrEqualThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate less than or equals to DEFAULT_BIRTH_DATE
        defaultUserInfoShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the userInfoList where birthDate less than or equals to UPDATED_BIRTH_DATE
        defaultUserInfoShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }


    @Test
    @Transactional
    public void getAllUserInfosByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where gender equals to DEFAULT_GENDER
        defaultUserInfoShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the userInfoList where gender equals to UPDATED_GENDER
        defaultUserInfoShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllUserInfosByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultUserInfoShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the userInfoList where gender equals to UPDATED_GENDER
        defaultUserInfoShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllUserInfosByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where gender is not null
        defaultUserInfoShouldBeFound("gender.specified=true");

        // Get all the userInfoList where gender is null
        defaultUserInfoShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userInfo.setUser(user);
        userInfoRepository.saveAndFlush(userInfo);
        Long userId = user.getId();

        // Get all the userInfoList where user equals to userId
        defaultUserInfoShouldBeFound("userId.equals=" + userId);

        // Get all the userInfoList where user equals to userId + 1
        defaultUserInfoShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserInfoShouldBeFound(String filter) throws Exception {
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].emailFlag").value(hasItem(DEFAULT_EMAIL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].smsFlag").value(hasItem(DEFAULT_SMS_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));

        // Check, that the count call also returns 1
        restUserInfoMockMvc.perform(get("/api/user-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserInfoShouldNotBeFound(String filter) throws Exception {
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserInfoMockMvc.perform(get("/api/user-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        // Initialize the database
        userInfoService.save(userInfo);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on updatedUserInfo are not directly saved in db
        em.detach(updatedUserInfo);
        updatedUserInfo
            .phone(UPDATED_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .emailFlag(UPDATED_EMAIL_FLAG)
            .smsFlag(UPDATED_SMS_FLAG)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER);

        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserInfo)))
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserInfo.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testUserInfo.isEmailFlag()).isEqualTo(UPDATED_EMAIL_FLAG);
        assertThat(testUserInfo.isSmsFlag()).isEqualTo(UPDATED_SMS_FLAG);
        assertThat(testUserInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUserInfo.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void updateNonExistingUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Create the UserInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserInfo() throws Exception {
        // Initialize the database
        userInfoService.save(userInfo);

        int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Delete the userInfo
        restUserInfoMockMvc.perform(delete("/api/user-infos/{id}", userInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInfo.class);
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(1L);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(userInfo1.getId());
        assertThat(userInfo1).isEqualTo(userInfo2);
        userInfo2.setId(2L);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
        userInfo1.setId(null);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
    }
}
