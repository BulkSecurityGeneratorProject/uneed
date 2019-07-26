package com.hongz.uneed.web.rest;

import com.hongz.uneed.UneedApp;
import com.hongz.uneed.domain.UserStat;
import com.hongz.uneed.domain.User;
import com.hongz.uneed.repository.UserStatRepository;
import com.hongz.uneed.service.UserStatService;
import com.hongz.uneed.web.rest.errors.ExceptionTranslator;
import com.hongz.uneed.service.dto.UserStatCriteria;
import com.hongz.uneed.service.UserStatQueryService;

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
import java.math.BigDecimal;
import java.util.List;

import static com.hongz.uneed.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UserStatResource} REST controller.
 */
@SpringBootTest(classes = UneedApp.class)
public class UserStatResourceIT {

    private static final Long DEFAULT_VIEW_COUNT = 1L;
    private static final Long UPDATED_VIEW_COUNT = 2L;

    private static final Integer DEFAULT_REVIEW_COUNT = 1;
    private static final Integer UPDATED_REVIEW_COUNT = 2;

    private static final BigDecimal DEFAULT_RATING = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATING = new BigDecimal(2);

    @Autowired
    private UserStatRepository userStatRepository;

    @Autowired
    private UserStatService userStatService;

    @Autowired
    private UserStatQueryService userStatQueryService;

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

    private MockMvc restUserStatMockMvc;

    private UserStat userStat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserStatResource userStatResource = new UserStatResource(userStatService, userStatQueryService);
        this.restUserStatMockMvc = MockMvcBuilders.standaloneSetup(userStatResource)
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
    public static UserStat createEntity(EntityManager em) {
        UserStat userStat = new UserStat()
            .viewCount(DEFAULT_VIEW_COUNT)
            .reviewCount(DEFAULT_REVIEW_COUNT)
            .rating(DEFAULT_RATING);
        return userStat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserStat createUpdatedEntity(EntityManager em) {
        UserStat userStat = new UserStat()
            .viewCount(UPDATED_VIEW_COUNT)
            .reviewCount(UPDATED_REVIEW_COUNT)
            .rating(UPDATED_RATING);
        return userStat;
    }

    @BeforeEach
    public void initTest() {
        userStat = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserStat() throws Exception {
        int databaseSizeBeforeCreate = userStatRepository.findAll().size();

        // Create the UserStat
        restUserStatMockMvc.perform(post("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStat)))
            .andExpect(status().isCreated());

        // Validate the UserStat in the database
        List<UserStat> userStatList = userStatRepository.findAll();
        assertThat(userStatList).hasSize(databaseSizeBeforeCreate + 1);
        UserStat testUserStat = userStatList.get(userStatList.size() - 1);
        assertThat(testUserStat.getViewCount()).isEqualTo(DEFAULT_VIEW_COUNT);
        assertThat(testUserStat.getReviewCount()).isEqualTo(DEFAULT_REVIEW_COUNT);
        assertThat(testUserStat.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createUserStatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userStatRepository.findAll().size();

        // Create the UserStat with an existing ID
        userStat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserStatMockMvc.perform(post("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStat)))
            .andExpect(status().isBadRequest());

        // Validate the UserStat in the database
        List<UserStat> userStatList = userStatRepository.findAll();
        assertThat(userStatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserStats() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList
        restUserStatMockMvc.perform(get("/api/user-stats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userStat.getId().intValue())))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].reviewCount").value(hasItem(DEFAULT_REVIEW_COUNT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.intValue())));
    }
    
    @Test
    @Transactional
    public void getUserStat() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get the userStat
        restUserStatMockMvc.perform(get("/api/user-stats/{id}", userStat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userStat.getId().intValue()))
            .andExpect(jsonPath("$.viewCount").value(DEFAULT_VIEW_COUNT.intValue()))
            .andExpect(jsonPath("$.reviewCount").value(DEFAULT_REVIEW_COUNT))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.intValue()));
    }

    @Test
    @Transactional
    public void getAllUserStatsByViewCountIsEqualToSomething() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where viewCount equals to DEFAULT_VIEW_COUNT
        defaultUserStatShouldBeFound("viewCount.equals=" + DEFAULT_VIEW_COUNT);

        // Get all the userStatList where viewCount equals to UPDATED_VIEW_COUNT
        defaultUserStatShouldNotBeFound("viewCount.equals=" + UPDATED_VIEW_COUNT);
    }

    @Test
    @Transactional
    public void getAllUserStatsByViewCountIsInShouldWork() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where viewCount in DEFAULT_VIEW_COUNT or UPDATED_VIEW_COUNT
        defaultUserStatShouldBeFound("viewCount.in=" + DEFAULT_VIEW_COUNT + "," + UPDATED_VIEW_COUNT);

        // Get all the userStatList where viewCount equals to UPDATED_VIEW_COUNT
        defaultUserStatShouldNotBeFound("viewCount.in=" + UPDATED_VIEW_COUNT);
    }

    @Test
    @Transactional
    public void getAllUserStatsByViewCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where viewCount is not null
        defaultUserStatShouldBeFound("viewCount.specified=true");

        // Get all the userStatList where viewCount is null
        defaultUserStatShouldNotBeFound("viewCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserStatsByViewCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where viewCount greater than or equals to DEFAULT_VIEW_COUNT
        defaultUserStatShouldBeFound("viewCount.greaterOrEqualThan=" + DEFAULT_VIEW_COUNT);

        // Get all the userStatList where viewCount greater than or equals to UPDATED_VIEW_COUNT
        defaultUserStatShouldNotBeFound("viewCount.greaterOrEqualThan=" + UPDATED_VIEW_COUNT);
    }

    @Test
    @Transactional
    public void getAllUserStatsByViewCountIsLessThanSomething() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where viewCount less than or equals to DEFAULT_VIEW_COUNT
        defaultUserStatShouldNotBeFound("viewCount.lessThan=" + DEFAULT_VIEW_COUNT);

        // Get all the userStatList where viewCount less than or equals to UPDATED_VIEW_COUNT
        defaultUserStatShouldBeFound("viewCount.lessThan=" + UPDATED_VIEW_COUNT);
    }


    @Test
    @Transactional
    public void getAllUserStatsByReviewCountIsEqualToSomething() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where reviewCount equals to DEFAULT_REVIEW_COUNT
        defaultUserStatShouldBeFound("reviewCount.equals=" + DEFAULT_REVIEW_COUNT);

        // Get all the userStatList where reviewCount equals to UPDATED_REVIEW_COUNT
        defaultUserStatShouldNotBeFound("reviewCount.equals=" + UPDATED_REVIEW_COUNT);
    }

    @Test
    @Transactional
    public void getAllUserStatsByReviewCountIsInShouldWork() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where reviewCount in DEFAULT_REVIEW_COUNT or UPDATED_REVIEW_COUNT
        defaultUserStatShouldBeFound("reviewCount.in=" + DEFAULT_REVIEW_COUNT + "," + UPDATED_REVIEW_COUNT);

        // Get all the userStatList where reviewCount equals to UPDATED_REVIEW_COUNT
        defaultUserStatShouldNotBeFound("reviewCount.in=" + UPDATED_REVIEW_COUNT);
    }

    @Test
    @Transactional
    public void getAllUserStatsByReviewCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where reviewCount is not null
        defaultUserStatShouldBeFound("reviewCount.specified=true");

        // Get all the userStatList where reviewCount is null
        defaultUserStatShouldNotBeFound("reviewCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserStatsByReviewCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where reviewCount greater than or equals to DEFAULT_REVIEW_COUNT
        defaultUserStatShouldBeFound("reviewCount.greaterOrEqualThan=" + DEFAULT_REVIEW_COUNT);

        // Get all the userStatList where reviewCount greater than or equals to UPDATED_REVIEW_COUNT
        defaultUserStatShouldNotBeFound("reviewCount.greaterOrEqualThan=" + UPDATED_REVIEW_COUNT);
    }

    @Test
    @Transactional
    public void getAllUserStatsByReviewCountIsLessThanSomething() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where reviewCount less than or equals to DEFAULT_REVIEW_COUNT
        defaultUserStatShouldNotBeFound("reviewCount.lessThan=" + DEFAULT_REVIEW_COUNT);

        // Get all the userStatList where reviewCount less than or equals to UPDATED_REVIEW_COUNT
        defaultUserStatShouldBeFound("reviewCount.lessThan=" + UPDATED_REVIEW_COUNT);
    }


    @Test
    @Transactional
    public void getAllUserStatsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where rating equals to DEFAULT_RATING
        defaultUserStatShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the userStatList where rating equals to UPDATED_RATING
        defaultUserStatShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllUserStatsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultUserStatShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the userStatList where rating equals to UPDATED_RATING
        defaultUserStatShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllUserStatsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        userStatRepository.saveAndFlush(userStat);

        // Get all the userStatList where rating is not null
        defaultUserStatShouldBeFound("rating.specified=true");

        // Get all the userStatList where rating is null
        defaultUserStatShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserStatsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userStat.setUser(user);
        userStatRepository.saveAndFlush(userStat);
        Long userId = user.getId();

        // Get all the userStatList where user equals to userId
        defaultUserStatShouldBeFound("userId.equals=" + userId);

        // Get all the userStatList where user equals to userId + 1
        defaultUserStatShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserStatShouldBeFound(String filter) throws Exception {
        restUserStatMockMvc.perform(get("/api/user-stats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userStat.getId().intValue())))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].reviewCount").value(hasItem(DEFAULT_REVIEW_COUNT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.intValue())));

        // Check, that the count call also returns 1
        restUserStatMockMvc.perform(get("/api/user-stats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserStatShouldNotBeFound(String filter) throws Exception {
        restUserStatMockMvc.perform(get("/api/user-stats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserStatMockMvc.perform(get("/api/user-stats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserStat() throws Exception {
        // Get the userStat
        restUserStatMockMvc.perform(get("/api/user-stats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserStat() throws Exception {
        // Initialize the database
        userStatService.save(userStat);

        int databaseSizeBeforeUpdate = userStatRepository.findAll().size();

        // Update the userStat
        UserStat updatedUserStat = userStatRepository.findById(userStat.getId()).get();
        // Disconnect from session so that the updates on updatedUserStat are not directly saved in db
        em.detach(updatedUserStat);
        updatedUserStat
            .viewCount(UPDATED_VIEW_COUNT)
            .reviewCount(UPDATED_REVIEW_COUNT)
            .rating(UPDATED_RATING);

        restUserStatMockMvc.perform(put("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserStat)))
            .andExpect(status().isOk());

        // Validate the UserStat in the database
        List<UserStat> userStatList = userStatRepository.findAll();
        assertThat(userStatList).hasSize(databaseSizeBeforeUpdate);
        UserStat testUserStat = userStatList.get(userStatList.size() - 1);
        assertThat(testUserStat.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
        assertThat(testUserStat.getReviewCount()).isEqualTo(UPDATED_REVIEW_COUNT);
        assertThat(testUserStat.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void updateNonExistingUserStat() throws Exception {
        int databaseSizeBeforeUpdate = userStatRepository.findAll().size();

        // Create the UserStat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserStatMockMvc.perform(put("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStat)))
            .andExpect(status().isBadRequest());

        // Validate the UserStat in the database
        List<UserStat> userStatList = userStatRepository.findAll();
        assertThat(userStatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserStat() throws Exception {
        // Initialize the database
        userStatService.save(userStat);

        int databaseSizeBeforeDelete = userStatRepository.findAll().size();

        // Delete the userStat
        restUserStatMockMvc.perform(delete("/api/user-stats/{id}", userStat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserStat> userStatList = userStatRepository.findAll();
        assertThat(userStatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStat.class);
        UserStat userStat1 = new UserStat();
        userStat1.setId(1L);
        UserStat userStat2 = new UserStat();
        userStat2.setId(userStat1.getId());
        assertThat(userStat1).isEqualTo(userStat2);
        userStat2.setId(2L);
        assertThat(userStat1).isNotEqualTo(userStat2);
        userStat1.setId(null);
        assertThat(userStat1).isNotEqualTo(userStat2);
    }
}
