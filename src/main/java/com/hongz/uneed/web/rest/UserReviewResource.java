package com.hongz.uneed.web.rest;

import com.hongz.uneed.domain.UserReview;
import com.hongz.uneed.service.UserReviewService;
import com.hongz.uneed.web.rest.errors.BadRequestAlertException;
import com.hongz.uneed.service.dto.UserReviewCriteria;
import com.hongz.uneed.service.UserReviewQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hongz.uneed.domain.UserReview}.
 */
@RestController
@RequestMapping("/api")
public class UserReviewResource {

    private final Logger log = LoggerFactory.getLogger(UserReviewResource.class);

    private static final String ENTITY_NAME = "userReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserReviewService userReviewService;

    private final UserReviewQueryService userReviewQueryService;

    public UserReviewResource(UserReviewService userReviewService, UserReviewQueryService userReviewQueryService) {
        this.userReviewService = userReviewService;
        this.userReviewQueryService = userReviewQueryService;
    }

    /**
     * {@code POST  /user-reviews} : Create a new userReview.
     *
     * @param userReview the userReview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userReview, or with status {@code 400 (Bad Request)} if the userReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-reviews")
    public ResponseEntity<UserReview> createUserReview(@Valid @RequestBody UserReview userReview) throws URISyntaxException {
        log.debug("REST request to save UserReview : {}", userReview);
        if (userReview.getId() != null) {
            throw new BadRequestAlertException("A new userReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserReview result = userReviewService.save(userReview);
        return ResponseEntity.created(new URI("/api/user-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-reviews} : Updates an existing userReview.
     *
     * @param userReview the userReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userReview,
     * or with status {@code 400 (Bad Request)} if the userReview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-reviews")
    public ResponseEntity<UserReview> updateUserReview(@Valid @RequestBody UserReview userReview) throws URISyntaxException {
        log.debug("REST request to update UserReview : {}", userReview);
        if (userReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserReview result = userReviewService.save(userReview);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userReview.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-reviews} : get all the userReviews.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userReviews in body.
     */
    @GetMapping("/user-reviews")
    public ResponseEntity<List<UserReview>> getAllUserReviews(UserReviewCriteria criteria) {
        log.debug("REST request to get UserReviews by criteria: {}", criteria);
        List<UserReview> entityList = userReviewQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /user-reviews/count} : count all the userReviews.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/user-reviews/count")
    public ResponseEntity<Long> countUserReviews(UserReviewCriteria criteria) {
        log.debug("REST request to count UserReviews by criteria: {}", criteria);
        return ResponseEntity.ok().body(userReviewQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-reviews/:id} : get the "id" userReview.
     *
     * @param id the id of the userReview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userReview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-reviews/{id}")
    public ResponseEntity<UserReview> getUserReview(@PathVariable Long id) {
        log.debug("REST request to get UserReview : {}", id);
        Optional<UserReview> userReview = userReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userReview);
    }

    /**
     * {@code DELETE  /user-reviews/:id} : delete the "id" userReview.
     *
     * @param id the id of the userReview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-reviews/{id}")
    public ResponseEntity<Void> deleteUserReview(@PathVariable Long id) {
        log.debug("REST request to delete UserReview : {}", id);
        userReviewService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
