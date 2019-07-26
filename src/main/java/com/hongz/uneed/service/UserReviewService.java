package com.hongz.uneed.service;

import com.hongz.uneed.domain.UserReview;
import com.hongz.uneed.repository.UserReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UserReview}.
 */
@Service
@Transactional
public class UserReviewService {

    private final Logger log = LoggerFactory.getLogger(UserReviewService.class);

    private final UserReviewRepository userReviewRepository;

    public UserReviewService(UserReviewRepository userReviewRepository) {
        this.userReviewRepository = userReviewRepository;
    }

    /**
     * Save a userReview.
     *
     * @param userReview the entity to save.
     * @return the persisted entity.
     */
    public UserReview save(UserReview userReview) {
        log.debug("Request to save UserReview : {}", userReview);
        return userReviewRepository.save(userReview);
    }

    /**
     * Get all the userReviews.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserReview> findAll() {
        log.debug("Request to get all UserReviews");
        return userReviewRepository.findAll();
    }


    /**
     * Get one userReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserReview> findOne(Long id) {
        log.debug("Request to get UserReview : {}", id);
        return userReviewRepository.findById(id);
    }

    /**
     * Delete the userReview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserReview : {}", id);
        userReviewRepository.deleteById(id);
    }
}
