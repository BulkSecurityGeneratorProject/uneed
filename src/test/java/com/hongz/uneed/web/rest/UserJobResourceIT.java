package com.hongz.uneed.web.rest;

import com.hongz.uneed.UneedApp;
import com.hongz.uneed.domain.UserJob;
import com.hongz.uneed.domain.Category;
import com.hongz.uneed.domain.Tag;
import com.hongz.uneed.domain.User;
import com.hongz.uneed.repository.UserJobRepository;
import com.hongz.uneed.service.UserJobService;
import com.hongz.uneed.service.dto.UserJobDTO;
import com.hongz.uneed.service.mapper.UserJobMapper;
import com.hongz.uneed.web.rest.errors.ExceptionTranslator;
import com.hongz.uneed.service.dto.UserJobCriteria;
import com.hongz.uneed.service.UserJobQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.hongz.uneed.web.rest.TestUtil.sameInstant;
import static com.hongz.uneed.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UserJobResource} REST controller.
 */
@SpringBootTest(classes = UneedApp.class)
public class UserJobResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private UserJobRepository userJobRepository;

    @Mock
    private UserJobRepository userJobRepositoryMock;

    @Autowired
    private UserJobMapper userJobMapper;

    @Mock
    private UserJobService userJobServiceMock;

    @Autowired
    private UserJobService userJobService;

    @Autowired
    private UserJobQueryService userJobQueryService;

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

    private MockMvc restUserJobMockMvc;

    private UserJob userJob;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserJobResource userJobResource = new UserJobResource(userJobService, userJobQueryService);
        this.restUserJobMockMvc = MockMvcBuilders.standaloneSetup(userJobResource)
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
    public static UserJob createEntity(EntityManager em) {
        UserJob userJob = new UserJob()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .currency(DEFAULT_CURRENCY)
            .imageUrl(DEFAULT_IMAGE_URL)
            .createDate(DEFAULT_CREATE_DATE)
            .lastUpdateDate(DEFAULT_LAST_UPDATE_DATE);
        return userJob;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserJob createUpdatedEntity(EntityManager em) {
        UserJob userJob = new UserJob()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .currency(UPDATED_CURRENCY)
            .imageUrl(UPDATED_IMAGE_URL)
            .createDate(UPDATED_CREATE_DATE)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE);
        return userJob;
    }

    @BeforeEach
    public void initTest() {
        userJob = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserJob() throws Exception {
        int databaseSizeBeforeCreate = userJobRepository.findAll().size();

        // Create the UserJob
        UserJobDTO userJobDTO = userJobMapper.toDto(userJob);
        restUserJobMockMvc.perform(post("/api/user-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userJobDTO)))
            .andExpect(status().isCreated());

        // Validate the UserJob in the database
        List<UserJob> userJobList = userJobRepository.findAll();
        assertThat(userJobList).hasSize(databaseSizeBeforeCreate + 1);
        UserJob testUserJob = userJobList.get(userJobList.size() - 1);
        assertThat(testUserJob.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserJob.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testUserJob.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testUserJob.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testUserJob.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testUserJob.getLastUpdateDate()).isEqualTo(DEFAULT_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void createUserJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userJobRepository.findAll().size();

        // Create the UserJob with an existing ID
        userJob.setId(1L);
        UserJobDTO userJobDTO = userJobMapper.toDto(userJob);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserJobMockMvc.perform(post("/api/user-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserJob in the database
        List<UserJob> userJobList = userJobRepository.findAll();
        assertThat(userJobList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userJobRepository.findAll().size();
        // set the field null
        userJob.setName(null);

        // Create the UserJob, which fails.
        UserJobDTO userJobDTO = userJobMapper.toDto(userJob);

        restUserJobMockMvc.perform(post("/api/user-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userJobDTO)))
            .andExpect(status().isBadRequest());

        List<UserJob> userJobList = userJobRepository.findAll();
        assertThat(userJobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserJobs() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList
        restUserJobMockMvc.perform(get("/api/user-jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdateDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE_DATE))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllUserJobsWithEagerRelationshipsIsEnabled() throws Exception {
        UserJobResource userJobResource = new UserJobResource(userJobServiceMock, userJobQueryService);
        when(userJobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restUserJobMockMvc = MockMvcBuilders.standaloneSetup(userJobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserJobMockMvc.perform(get("/api/user-jobs?eagerload=true"))
        .andExpect(status().isOk());

        verify(userJobServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllUserJobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        UserJobResource userJobResource = new UserJobResource(userJobServiceMock, userJobQueryService);
            when(userJobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restUserJobMockMvc = MockMvcBuilders.standaloneSetup(userJobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserJobMockMvc.perform(get("/api/user-jobs?eagerload=true"))
        .andExpect(status().isOk());

            verify(userJobServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserJob() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get the userJob
        restUserJobMockMvc.perform(get("/api/user-jobs/{id}", userJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userJob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.lastUpdateDate").value(sameInstant(DEFAULT_LAST_UPDATE_DATE)));
    }

    @Test
    @Transactional
    public void getAllUserJobsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where name equals to DEFAULT_NAME
        defaultUserJobShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the userJobList where name equals to UPDATED_NAME
        defaultUserJobShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserJobsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUserJobShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the userJobList where name equals to UPDATED_NAME
        defaultUserJobShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserJobsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where name is not null
        defaultUserJobShouldBeFound("name.specified=true");

        // Get all the userJobList where name is null
        defaultUserJobShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserJobsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where description equals to DEFAULT_DESCRIPTION
        defaultUserJobShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the userJobList where description equals to UPDATED_DESCRIPTION
        defaultUserJobShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserJobsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUserJobShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the userJobList where description equals to UPDATED_DESCRIPTION
        defaultUserJobShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserJobsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where description is not null
        defaultUserJobShouldBeFound("description.specified=true");

        // Get all the userJobList where description is null
        defaultUserJobShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserJobsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where price equals to DEFAULT_PRICE
        defaultUserJobShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the userJobList where price equals to UPDATED_PRICE
        defaultUserJobShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultUserJobShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the userJobList where price equals to UPDATED_PRICE
        defaultUserJobShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where price is not null
        defaultUserJobShouldBeFound("price.specified=true");

        // Get all the userJobList where price is null
        defaultUserJobShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserJobsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where currency equals to DEFAULT_CURRENCY
        defaultUserJobShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the userJobList where currency equals to UPDATED_CURRENCY
        defaultUserJobShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllUserJobsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultUserJobShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the userJobList where currency equals to UPDATED_CURRENCY
        defaultUserJobShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllUserJobsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where currency is not null
        defaultUserJobShouldBeFound("currency.specified=true");

        // Get all the userJobList where currency is null
        defaultUserJobShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserJobsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultUserJobShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the userJobList where imageUrl equals to UPDATED_IMAGE_URL
        defaultUserJobShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllUserJobsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultUserJobShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the userJobList where imageUrl equals to UPDATED_IMAGE_URL
        defaultUserJobShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllUserJobsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where imageUrl is not null
        defaultUserJobShouldBeFound("imageUrl.specified=true");

        // Get all the userJobList where imageUrl is null
        defaultUserJobShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserJobsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where createDate equals to DEFAULT_CREATE_DATE
        defaultUserJobShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the userJobList where createDate equals to UPDATED_CREATE_DATE
        defaultUserJobShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultUserJobShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the userJobList where createDate equals to UPDATED_CREATE_DATE
        defaultUserJobShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where createDate is not null
        defaultUserJobShouldBeFound("createDate.specified=true");

        // Get all the userJobList where createDate is null
        defaultUserJobShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserJobsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where createDate greater than or equals to DEFAULT_CREATE_DATE
        defaultUserJobShouldBeFound("createDate.greaterOrEqualThan=" + DEFAULT_CREATE_DATE);

        // Get all the userJobList where createDate greater than or equals to UPDATED_CREATE_DATE
        defaultUserJobShouldNotBeFound("createDate.greaterOrEqualThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where createDate less than or equals to DEFAULT_CREATE_DATE
        defaultUserJobShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the userJobList where createDate less than or equals to UPDATED_CREATE_DATE
        defaultUserJobShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }


    @Test
    @Transactional
    public void getAllUserJobsByLastUpdateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where lastUpdateDate equals to DEFAULT_LAST_UPDATE_DATE
        defaultUserJobShouldBeFound("lastUpdateDate.equals=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the userJobList where lastUpdateDate equals to UPDATED_LAST_UPDATE_DATE
        defaultUserJobShouldNotBeFound("lastUpdateDate.equals=" + UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByLastUpdateDateIsInShouldWork() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where lastUpdateDate in DEFAULT_LAST_UPDATE_DATE or UPDATED_LAST_UPDATE_DATE
        defaultUserJobShouldBeFound("lastUpdateDate.in=" + DEFAULT_LAST_UPDATE_DATE + "," + UPDATED_LAST_UPDATE_DATE);

        // Get all the userJobList where lastUpdateDate equals to UPDATED_LAST_UPDATE_DATE
        defaultUserJobShouldNotBeFound("lastUpdateDate.in=" + UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByLastUpdateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where lastUpdateDate is not null
        defaultUserJobShouldBeFound("lastUpdateDate.specified=true");

        // Get all the userJobList where lastUpdateDate is null
        defaultUserJobShouldNotBeFound("lastUpdateDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserJobsByLastUpdateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where lastUpdateDate greater than or equals to DEFAULT_LAST_UPDATE_DATE
        defaultUserJobShouldBeFound("lastUpdateDate.greaterOrEqualThan=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the userJobList where lastUpdateDate greater than or equals to UPDATED_LAST_UPDATE_DATE
        defaultUserJobShouldNotBeFound("lastUpdateDate.greaterOrEqualThan=" + UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllUserJobsByLastUpdateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        // Get all the userJobList where lastUpdateDate less than or equals to DEFAULT_LAST_UPDATE_DATE
        defaultUserJobShouldNotBeFound("lastUpdateDate.lessThan=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the userJobList where lastUpdateDate less than or equals to UPDATED_LAST_UPDATE_DATE
        defaultUserJobShouldBeFound("lastUpdateDate.lessThan=" + UPDATED_LAST_UPDATE_DATE);
    }


    @Test
    @Transactional
    public void getAllUserJobsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        userJob.setCategory(category);
        userJobRepository.saveAndFlush(userJob);
        Long categoryId = category.getId();

        // Get all the userJobList where category equals to categoryId
        defaultUserJobShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the userJobList where category equals to categoryId + 1
        defaultUserJobShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllUserJobsByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        Tag tag = TagResourceIT.createEntity(em);
        em.persist(tag);
        em.flush();
        userJob.addTag(tag);
        userJobRepository.saveAndFlush(userJob);
        Long tagId = tag.getId();

        // Get all the userJobList where tag equals to tagId
        defaultUserJobShouldBeFound("tagId.equals=" + tagId);

        // Get all the userJobList where tag equals to tagId + 1
        defaultUserJobShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }


    @Test
    @Transactional
    public void getAllUserJobsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userJob.setUser(user);
        userJobRepository.saveAndFlush(userJob);
        Long userId = user.getId();

        // Get all the userJobList where user equals to userId
        defaultUserJobShouldBeFound("userId.equals=" + userId);

        // Get all the userJobList where user equals to userId + 1
        defaultUserJobShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserJobShouldBeFound(String filter) throws Exception {
        restUserJobMockMvc.perform(get("/api/user-jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdateDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE_DATE))));

        // Check, that the count call also returns 1
        restUserJobMockMvc.perform(get("/api/user-jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserJobShouldNotBeFound(String filter) throws Exception {
        restUserJobMockMvc.perform(get("/api/user-jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserJobMockMvc.perform(get("/api/user-jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserJob() throws Exception {
        // Get the userJob
        restUserJobMockMvc.perform(get("/api/user-jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserJob() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        int databaseSizeBeforeUpdate = userJobRepository.findAll().size();

        // Update the userJob
        UserJob updatedUserJob = userJobRepository.findById(userJob.getId()).get();
        // Disconnect from session so that the updates on updatedUserJob are not directly saved in db
        em.detach(updatedUserJob);
        updatedUserJob
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .currency(UPDATED_CURRENCY)
            .imageUrl(UPDATED_IMAGE_URL)
            .createDate(UPDATED_CREATE_DATE)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE);
        UserJobDTO userJobDTO = userJobMapper.toDto(updatedUserJob);

        restUserJobMockMvc.perform(put("/api/user-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userJobDTO)))
            .andExpect(status().isOk());

        // Validate the UserJob in the database
        List<UserJob> userJobList = userJobRepository.findAll();
        assertThat(userJobList).hasSize(databaseSizeBeforeUpdate);
        UserJob testUserJob = userJobList.get(userJobList.size() - 1);
        assertThat(testUserJob.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserJob.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testUserJob.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testUserJob.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUserJob.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testUserJob.getLastUpdateDate()).isEqualTo(UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserJob() throws Exception {
        int databaseSizeBeforeUpdate = userJobRepository.findAll().size();

        // Create the UserJob
        UserJobDTO userJobDTO = userJobMapper.toDto(userJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserJobMockMvc.perform(put("/api/user-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserJob in the database
        List<UserJob> userJobList = userJobRepository.findAll();
        assertThat(userJobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserJob() throws Exception {
        // Initialize the database
        userJobRepository.saveAndFlush(userJob);

        int databaseSizeBeforeDelete = userJobRepository.findAll().size();

        // Delete the userJob
        restUserJobMockMvc.perform(delete("/api/user-jobs/{id}", userJob.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserJob> userJobList = userJobRepository.findAll();
        assertThat(userJobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserJob.class);
        UserJob userJob1 = new UserJob();
        userJob1.setId(1L);
        UserJob userJob2 = new UserJob();
        userJob2.setId(userJob1.getId());
        assertThat(userJob1).isEqualTo(userJob2);
        userJob2.setId(2L);
        assertThat(userJob1).isNotEqualTo(userJob2);
        userJob1.setId(null);
        assertThat(userJob1).isNotEqualTo(userJob2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserJobDTO.class);
        UserJobDTO userJobDTO1 = new UserJobDTO();
        userJobDTO1.setId(1L);
        UserJobDTO userJobDTO2 = new UserJobDTO();
        assertThat(userJobDTO1).isNotEqualTo(userJobDTO2);
        userJobDTO2.setId(userJobDTO1.getId());
        assertThat(userJobDTO1).isEqualTo(userJobDTO2);
        userJobDTO2.setId(2L);
        assertThat(userJobDTO1).isNotEqualTo(userJobDTO2);
        userJobDTO1.setId(null);
        assertThat(userJobDTO1).isNotEqualTo(userJobDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userJobMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userJobMapper.fromId(null)).isNull();
    }
}
