package com.hongz.uneed.repository;

import com.hongz.uneed.domain.UserInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo> {

    @Query("select userInfo from UserInfo userInfo where userInfo.user.login = ?#{principal.username}")
    List<UserInfo> findByUserIsCurrentUser();
}
