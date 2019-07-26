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

import com.hongz.uneed.domain.UserStat;
import com.hongz.uneed.domain.*; // for static metamodels
import com.hongz.uneed.repository.UserStatRepository;
import com.hongz.uneed.service.dto.UserStatCriteria;

/**
 * Service for executing complex queries for {@link UserStat} entities in the database.
 * The main input is a {@link UserStatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserStat} or a {@link Page} of {@link UserStat} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserStatQueryService extends QueryService<UserStat> {

    private final Logger log = LoggerFactory.getLogger(UserStatQueryService.class);

    private final UserStatRepository userStatRepository;

    public UserStatQueryService(UserStatRepository userStatRepository) {
        this.userStatRepository = userStatRepository;
    }

    /**
     * Return a {@link List} of {@link UserStat} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserStat> findByCriteria(UserStatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserStat> specification = createSpecification(criteria);
        return userStatRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserStat} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserStat> findByCriteria(UserStatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserStat> specification = createSpecification(criteria);
        return userStatRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserStatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserStat> specification = createSpecification(criteria);
        return userStatRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<UserStat> createSpecification(UserStatCriteria criteria) {
        Specification<UserStat> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserStat_.id));
            }
            if (criteria.getViewCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getViewCount(), UserStat_.viewCount));
            }
            if (criteria.getReviewCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewCount(), UserStat_.reviewCount));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), UserStat_.rating));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(UserStat_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
