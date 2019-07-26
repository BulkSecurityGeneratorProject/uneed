package com.hongz.uneed.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.hongz.uneed.domain.UserReview;
import com.hongz.uneed.domain.*; // for static metamodels
import com.hongz.uneed.repository.UserReviewRepository;
import com.hongz.uneed.service.dto.UserReviewCriteria;

/**
 * Service for executing complex queries for {@link UserReview} entities in the database.
 * The main input is a {@link UserReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserReview} or a {@link Page} of {@link UserReview} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserReviewQueryService extends QueryService<UserReview> {

    private final Logger log = LoggerFactory.getLogger(UserReviewQueryService.class);

    private final UserReviewRepository userReviewRepository;

    public UserReviewQueryService(UserReviewRepository userReviewRepository) {
        this.userReviewRepository = userReviewRepository;
    }

    /**
     * Return a {@link List} of {@link UserReview} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserReview> findByCriteria(UserReviewCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserReview> specification = createSpecification(criteria);
        return userReviewRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserReview} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserReview> findByCriteria(UserReviewCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserReview> specification = createSpecification(criteria);
        return userReviewRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserReviewCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserReview> specification = createSpecification(criteria);
        return userReviewRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<UserReview> createSpecification(UserReviewCriteria criteria) {
        Specification<UserReview> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserReview_.id));
            }
            if (criteria.getScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScore(), UserReview_.score));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), UserReview_.comment));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), UserReview_.date));
            }
            if (criteria.getReviewer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReviewer(), UserReview_.reviewer));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(UserReview_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
