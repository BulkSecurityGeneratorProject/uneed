package com.hongz.uneed.web.rest;

import com.hongz.uneed.domain.UserInfo;
import com.hongz.uneed.service.UserInfoService;
import com.hongz.uneed.web.rest.errors.BadRequestAlertException;
import com.hongz.uneed.service.dto.UserInfoCriteria;
import com.hongz.uneed.service.UserInfoQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hongz.uneed.domain.UserInfo}.
 */
@RestController
@RequestMapping("/api")
public class UserInfoResource {

    private final Logger log = LoggerFactory.getLogger(UserInfoResource.class);

    private static final String ENTITY_NAME = "userInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserInfoService userInfoService;

    private final UserInfoQueryService userInfoQueryService;

    public UserInfoResource(UserInfoService userInfoService, UserInfoQueryService userInfoQueryService) {
        this.userInfoService = userInfoService;
        this.userInfoQueryService = userInfoQueryService;
    }

    /**
     * {@code POST  /user-infos} : Create a new userInfo.
     *
     * @param userInfo the userInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userInfo, or with status {@code 400 (Bad Request)} if the userInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-infos")
    public ResponseEntity<UserInfo> createUserInfo(@RequestBody UserInfo userInfo) throws URISyntaxException {
        log.debug("REST request to save UserInfo : {}", userInfo);
        if (userInfo.getId() != null) {
            throw new BadRequestAlertException("A new userInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserInfo result = userInfoService.save(userInfo);
        return ResponseEntity.created(new URI("/api/user-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-infos} : Updates an existing userInfo.
     *
     * @param userInfo the userInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userInfo,
     * or with status {@code 400 (Bad Request)} if the userInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-infos")
    public ResponseEntity<UserInfo> updateUserInfo(@RequestBody UserInfo userInfo) throws URISyntaxException {
        log.debug("REST request to update UserInfo : {}", userInfo);
        if (userInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserInfo result = userInfoService.save(userInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-infos} : get all the userInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userInfos in body.
     */
    @GetMapping("/user-infos")
    public ResponseEntity<List<UserInfo>> getAllUserInfos(UserInfoCriteria criteria) {
        log.debug("REST request to get UserInfos by criteria: {}", criteria);
        List<UserInfo> entityList = userInfoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /user-infos/count} : count all the userInfos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/user-infos/count")
    public ResponseEntity<Long> countUserInfos(UserInfoCriteria criteria) {
        log.debug("REST request to count UserInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(userInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-infos/:id} : get the "id" userInfo.
     *
     * @param id the id of the userInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-infos/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable Long id) {
        log.debug("REST request to get UserInfo : {}", id);
        Optional<UserInfo> userInfo = userInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userInfo);
    }

    /**
     * {@code DELETE  /user-infos/:id} : delete the "id" userInfo.
     *
     * @param id the id of the userInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-infos/{id}")
    public ResponseEntity<Void> deleteUserInfo(@PathVariable Long id) {
        log.debug("REST request to delete UserInfo : {}", id);
        userInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
