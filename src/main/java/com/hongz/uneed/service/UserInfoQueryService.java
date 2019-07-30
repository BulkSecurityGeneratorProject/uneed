package com.hongz.uneed.service;

import com.hongz.uneed.domain.UserInfo;
import com.hongz.uneed.domain.UserInfo_;
import com.hongz.uneed.domain.User_;
import com.hongz.uneed.repository.UserInfoRepository;
import com.hongz.uneed.security.AuthoritiesConstants;
import com.hongz.uneed.security.SecurityUtils;
import com.hongz.uneed.service.dto.UserInfoCriteria;
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
 * Service for executing complex queries for {@link UserInfo} entities in the database.
 * The main input is a {@link UserInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserInfo} or a {@link Page} of {@link UserInfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserInfoQueryService extends QueryService<UserInfo> {

    private final Logger log = LoggerFactory.getLogger(UserInfoQueryService.class);

    private final UserInfoRepository userInfoRepository;

    public UserInfoQueryService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    /**
     * Return a {@link List} of {@link UserInfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserInfo> findByCriteria(UserInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("Not admin, using current user: {}", SecurityUtils.getCurrentUserLogin());
            return userInfoRepository.findByUserIsCurrentUser();
        } else {
            final Specification<UserInfo> specification = createSpecification(criteria);
            return userInfoRepository.findAll(specification);
        }
    }

    /**
     * Return a {@link Page} of {@link UserInfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserInfo> findByCriteria(UserInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserInfo> specification = createSpecification(criteria);
        return userInfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserInfo> specification = createSpecification(criteria);
        return userInfoRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<UserInfo> createSpecification(UserInfoCriteria criteria) {
        Specification<UserInfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserInfo_.id));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), UserInfo_.phone));
            }
            if (criteria.getMobilePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobilePhone(), UserInfo_.mobilePhone));
            }
            if (criteria.getEmailFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getEmailFlag(), UserInfo_.emailFlag));
            }
            if (criteria.getSmsFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getSmsFlag(), UserInfo_.smsFlag));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), UserInfo_.birthDate));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), UserInfo_.gender));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(UserInfo_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
