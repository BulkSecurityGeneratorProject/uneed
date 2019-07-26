package com.hongz.uneed.repository;

import com.hongz.uneed.domain.UserJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserJobRepository extends JpaRepository<UserJob, Long>, JpaSpecificationExecutor<UserJob> {

}
