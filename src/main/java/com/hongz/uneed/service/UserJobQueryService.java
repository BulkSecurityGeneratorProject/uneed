package com.hongz.uneed.service;

import com.hongz.uneed.domain.*;
import com.hongz.uneed.repository.UserJobRepository;
import com.hongz.uneed.security.AuthoritiesConstants;
import com.hongz.uneed.security.SecurityUtils;
import com.hongz.uneed.service.dto.UserJobCriteria;
import com.hongz.uneed.service.dto.UserJobDTO;
import com.hongz.uneed.service.mapper.UserJobMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link UserJob} entities in the database.
 * The main input is a {@link UserJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserJobDTO} or a {@link Page} of {@link UserJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserJobQueryService extends QueryService<UserJob> {

    private final Logger log = LoggerFactory.getLogger(UserJobQueryService.class);

    private final UserJobRepository userJobRepository;

    private final UserJobMapper userJobMapper;

    public UserJobQueryService(UserJobRepository userJobRepository, UserJobMapper userJobMapper) {
        this.userJobRepository = userJobRepository;
        this.userJobMapper = userJobMapper;
    }

    /**
     * Return a {@link List} of {@link UserJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserJobDTO> findByCriteria(UserJobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserJob> specification = createSpecification(criteria);
        return userJobMapper.toDto(userJobRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserJobDTO> findByCriteria(UserJobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("Not admin, using current user: {}", SecurityUtils.getCurrentUserLogin());
            return userJobRepository.findByUserIsCurrentUser(page)
                .map(userJobMapper::toDto);
        } else {
            return findByCriteriaPublic(criteria, page);
        }
    }

    @Transactional(readOnly = true)
    public Page<UserJobDTO> findByCriteriaPublic(UserJobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserJob> specification = createSpecification(criteria);
        return userJobRepository.findAll(specification, page)
            .map(userJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserJobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserJob> specification = createSpecification(criteria);
        return userJobRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<UserJob> createSpecification(UserJobCriteria criteria) {
        Specification<UserJob> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserJob_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getName(), UserJob_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.or(buildStringSpecification(criteria.getDescription(), UserJob_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), UserJob_.price));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), UserJob_.currency));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), UserJob_.imageUrl));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), UserJob_.createDate));
            }
            if (criteria.getLastUpdateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdateDate(), UserJob_.lastUpdateDate));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(UserJob_.category, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagId(),
                    root -> root.join(UserJob_.tags, JoinType.LEFT).get(Tag_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(UserJob_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
