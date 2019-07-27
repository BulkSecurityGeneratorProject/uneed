package com.hongz.uneed.repository;

import com.hongz.uneed.domain.UserJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserJob entity.
 */
@Repository
public interface UserJobRepository extends JpaRepository<UserJob, Long>, JpaSpecificationExecutor<UserJob> {

    @Query(value = "select distinct userJob from UserJob userJob left join fetch userJob.tags",
        countQuery = "select count(distinct userJob) from UserJob userJob")
    Page<UserJob> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userJob from UserJob userJob left join fetch userJob.tags")
    List<UserJob> findAllWithEagerRelationships();

    @Query("select userJob from UserJob userJob left join fetch userJob.tags where userJob.id =:id")
    Optional<UserJob> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select userJob from UserJob userJob where userJob.user.login = ?#{principal.username}")
    List<UserJob> findByUserIsCurrentUser();
}
