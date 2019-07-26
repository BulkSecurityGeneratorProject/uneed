package com.hongz.uneed.repository;

import com.hongz.uneed.domain.UserStat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserStat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserStatRepository extends JpaRepository<UserStat, Long>, JpaSpecificationExecutor<UserStat> {

}
