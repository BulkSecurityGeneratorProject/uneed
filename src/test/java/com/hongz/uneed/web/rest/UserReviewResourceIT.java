package com.hongz.uneed.web.rest;

import com.hongz.uneed.UneedApp;
import com.hongz.uneed.domain.UserReview;
import com.hongz.uneed.domain.User;
import com.hongz.uneed.repository.UserReviewRepository;
import com.hongz.uneed.service.UserReviewService;
import com.hongz.uneed.web.rest.errors.ExceptionTranslator;
import com.hongz.uneed.service.dto.UserReviewCriteria;
import com.hongz.uneed.service.UserReviewQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.hongz.uneed.web.rest.TestUtil.sameInstant;
import static com.hongz.uneed.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UserReviewResource} REST controller.
 */
@SpringBootTest(classes = UneedApp.class)
public class UserReviewResourceIT {

    private static final Integer DEFAULT_SCORE = 0;
    private static final Integer UPDATED_SCORE = 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_REVIEWER = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER = "BBBBBBBBBB";

    @Autowired
    private UserReviewRepository userReviewRepository;

    @Autowired
    private UserReviewService userReviewService;

    @Autowired
    private UserReviewQueryService userReviewQueryService;

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

    private MockMvc restUserReviewMockMvc;

    private UserReview userReview;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserReviewResource userReviewResource = new UserReviewResource(userReviewService, userReviewQueryService);
        this.restUserReviewMockMvc = MockMvcBuilders.standaloneSetup(userReviewResource)
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
    public static UserReview createEntity(EntityManager em) {
        UserReview userReview = new UserReview()
            .score(DEFAULT_SCORE)
            .comment(DEFAULT_COMMENT)
            .date(DEFAULT_DATE)
            .reviewer(DEFAULT_REVIEWER);
        return userReview;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReview createUpdatedEntity(EntityManager em) {
        UserReview userReview = new UserReview()
            .score(UPDATED_SCORE)
            .comment(UPDATED_COMMENT)
            .date(UPDATED_DATE)
            .reviewer(UPDATED_REVIEWER);
        return userReview;
    }

    @BeforeEach
    public void initTest() {
        userReview = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserReview() throws Exception {
        int databaseSizeBeforeCreate = userReviewRepository.findAll().size();

        // Create the UserReview
        restUserReviewMockMvc.perform(post("/api/user-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReview)))
            .andExpect(status().isCreated());

        // Validate the UserReview in the database
        List<UserReview> userReviewList = userReviewRepository.findAll();
        assertThat(userReviewList).hasSize(databaseSizeBeforeCreate + 1);
        UserReview testUserReview = userReviewList.get(userReviewList.size() - 1);
        assertThat(testUserReview.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testUserReview.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testUserReview.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserReview.getReviewer()).isEqualTo(DEFAULT_REVIEWER);
    }

    @Test
    @Transactional
    public void createUserReviewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userReviewRepository.findAll().size();

        // Create the UserReview with an existing ID
        userReview.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserReviewMockMvc.perform(post("/api/user-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReview)))
            .andExpect(status().isBadRequest());

        // Validate the UserReview in the database
        List<UserReview> userReviewList = userReviewRepository.findAll();
        assertThat(userReviewList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReviewRepository.findAll().size();
        // set the field null
        userReview.setScore(null);

        // Create the UserReview, which fails.

        restUserReviewMockMvc.perform(post("/api/user-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReview)))
            .andExpect(status().isBadRequest());

        List<UserReview> userReviewList = userReviewRepository.findAll();
        assertThat(userReviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserReviews() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList
        restUserReviewMockMvc.perform(get("/api/user-reviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER.toString())));
    }
    
    @Test
    @Transactional
    public void getUserReview() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get the userReview
        restUserReviewMockMvc.perform(get("/api/user-reviews/{id}", userReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userReview.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.reviewer").value(DEFAULT_REVIEWER.toString()));
    }

    @Test
    @Transactional
    public void getAllUserReviewsByScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where score equals to DEFAULT_SCORE
        defaultUserReviewShouldBeFound("score.equals=" + DEFAULT_SCORE);

        // Get all the userReviewList where score equals to UPDATED_SCORE
        defaultUserReviewShouldNotBeFound("score.equals=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByScoreIsInShouldWork() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where score in DEFAULT_SCORE or UPDATED_SCORE
        defaultUserReviewShouldBeFound("score.in=" + DEFAULT_SCORE + "," + UPDATED_SCORE);

        // Get all the userReviewList where score equals to UPDATED_SCORE
        defaultUserReviewShouldNotBeFound("score.in=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where score is not null
        defaultUserReviewShouldBeFound("score.specified=true");

        // Get all the userReviewList where score is null
        defaultUserReviewShouldNotBeFound("score.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserReviewsByScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where score greater than or equals to DEFAULT_SCORE
        defaultUserReviewShouldBeFound("score.greaterOrEqualThan=" + DEFAULT_SCORE);

        // Get all the userReviewList where score greater than or equals to (DEFAULT_SCORE + 1)
        defaultUserReviewShouldNotBeFound("score.greaterOrEqualThan=" + (DEFAULT_SCORE + 1));
    }

    @Test
    @Transactional
    public void getAllUserReviewsByScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where score less than or equals to DEFAULT_SCORE
        defaultUserReviewShouldNotBeFound("score.lessThan=" + DEFAULT_SCORE);

        // Get all the userReviewList where score less than or equals to (DEFAULT_SCORE + 1)
        defaultUserReviewShouldBeFound("score.lessThan=" + (DEFAULT_SCORE + 1));
    }


    @Test
    @Transactional
    public void getAllUserReviewsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where comment equals to DEFAULT_COMMENT
        defaultUserReviewShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the userReviewList where comment equals to UPDATED_COMMENT
        defaultUserReviewShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultUserReviewShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the userReviewList where comment equals to UPDATED_COMMENT
        defaultUserReviewShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where comment is not null
        defaultUserReviewShouldBeFound("comment.specified=true");

        // Get all the userReviewList where comment is null
        defaultUserReviewShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserReviewsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where date equals to DEFAULT_DATE
        defaultUserReviewShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the userReviewList where date equals to UPDATED_DATE
        defaultUserReviewShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where date in DEFAULT_DATE or UPDATED_DATE
        defaultUserReviewShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the userReviewList where date equals to UPDATED_DATE
        defaultUserReviewShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where date is not null
        defaultUserReviewShouldBeFound("date.specified=true");

        // Get all the userReviewList where date is null
        defaultUserReviewShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserReviewsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where date greater than or equals to DEFAULT_DATE
        defaultUserReviewShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the userReviewList where date greater than or equals to UPDATED_DATE
        defaultUserReviewShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where date less than or equals to DEFAULT_DATE
        defaultUserReviewShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the userReviewList where date less than or equals to UPDATED_DATE
        defaultUserReviewShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllUserReviewsByReviewerIsEqualToSomething() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where reviewer equals to DEFAULT_REVIEWER
        defaultUserReviewShouldBeFound("reviewer.equals=" + DEFAULT_REVIEWER);

        // Get all the userReviewList where reviewer equals to UPDATED_REVIEWER
        defaultUserReviewShouldNotBeFound("reviewer.equals=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByReviewerIsInShouldWork() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where reviewer in DEFAULT_REVIEWER or UPDATED_REVIEWER
        defaultUserReviewShouldBeFound("reviewer.in=" + DEFAULT_REVIEWER + "," + UPDATED_REVIEWER);

        // Get all the userReviewList where reviewer equals to UPDATED_REVIEWER
        defaultUserReviewShouldNotBeFound("reviewer.in=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllUserReviewsByReviewerIsNullOrNotNull() throws Exception {
        // Initialize the database
        userReviewRepository.saveAndFlush(userReview);

        // Get all the userReviewList where reviewer is not null
        defaultUserReviewShouldBeFound("reviewer.specified=true");

        // Get all the userReviewList where reviewer is null
        defaultUserReviewShouldNotBeFound("reviewer.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserReviewsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userReview.setUser(user);
        userReviewRepository.saveAndFlush(userReview);
        Long userId = user.getId();

        // Get all the userReviewList where user equals to userId
        defaultUserReviewShouldBeFound("userId.equals=" + userId);

        // Get all the userReviewList where user equals to userId + 1
        defaultUserReviewShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserReviewShouldBeFound(String filter) throws Exception {
        restUserReviewMockMvc.perform(get("/api/user-reviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER)));

        // Check, that the count call also returns 1
        restUserReviewMockMvc.perform(get("/api/user-reviews/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserReviewShouldNotBeFound(String filter) throws Exception {
        restUserReviewMockMvc.perform(get("/api/user-reviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserReviewMockMvc.perform(get("/api/user-reviews/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserReview() throws Exception {
        // Get the userReview
        restUserReviewMockMvc.perform(get("/api/user-reviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserReview() throws Exception {
        // Initialize the database
        userReviewService.save(userReview);

        int databaseSizeBeforeUpdate = userReviewRepository.findAll().size();

        // Update the userReview
        UserReview updatedUserReview = userReviewRepository.findById(userReview.getId()).get();
        // Disconnect from session so that the updates on updatedUserReview are not directly saved in db
        em.detach(updatedUserReview);
        updatedUserReview
            .score(UPDATED_SCORE)
            .comment(UPDATED_COMMENT)
            .date(UPDATED_DATE)
            .reviewer(UPDATED_REVIEWER);

        restUserReviewMockMvc.perform(put("/api/user-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserReview)))
            .andExpect(status().isOk());

        // Validate the UserReview in the database
        List<UserReview> userReviewList = userReviewRepository.findAll();
        assertThat(userReviewList).hasSize(databaseSizeBeforeUpdate);
        UserReview testUserReview = userReviewList.get(userReviewList.size() - 1);
        assertThat(testUserReview.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testUserReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testUserReview.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserReview.getReviewer()).isEqualTo(UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void updateNonExistingUserReview() throws Exception {
        int databaseSizeBeforeUpdate = userReviewRepository.findAll().size();

        // Create the UserReview

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserReviewMockMvc.perform(put("/api/user-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReview)))
            .andExpect(status().isBadRequest());

        // Validate the UserReview in the database
        List<UserReview> userReviewList = userReviewRepository.findAll();
        assertThat(userReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserReview() throws Exception {
        // Initialize the database
        userReviewService.save(userReview);

        int databaseSizeBeforeDelete = userReviewRepository.findAll().size();

        // Delete the userReview
        restUserReviewMockMvc.perform(delete("/api/user-reviews/{id}", userReview.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserReview> userReviewList = userReviewRepository.findAll();
        assertThat(userReviewList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReview.class);
        UserReview userReview1 = new UserReview();
        userReview1.setId(1L);
        UserReview userReview2 = new UserReview();
        userReview2.setId(userReview1.getId());
        assertThat(userReview1).isEqualTo(userReview2);
        userReview2.setId(2L);
        assertThat(userReview1).isNotEqualTo(userReview2);
        userReview1.setId(null);
        assertThat(userReview1).isNotEqualTo(userReview2);
    }
}
