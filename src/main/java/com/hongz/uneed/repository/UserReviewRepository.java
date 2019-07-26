package com.hongz.uneed.repository;

import com.hongz.uneed.domain.UserReview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long>, JpaSpecificationExecutor<UserReview> {

    @Query("select userReview from UserReview userReview where userReview.user.login = ?#{principal.username}")
    List<UserReview> findByUserIsCurrentUser();

}
