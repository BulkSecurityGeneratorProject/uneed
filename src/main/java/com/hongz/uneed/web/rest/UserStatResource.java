package com.hongz.uneed.web.rest;

import com.hongz.uneed.domain.UserStat;
import com.hongz.uneed.service.UserStatService;
import com.hongz.uneed.web.rest.errors.BadRequestAlertException;
import com.hongz.uneed.service.dto.UserStatCriteria;
import com.hongz.uneed.service.UserStatQueryService;

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
 * REST controller for managing {@link com.hongz.uneed.domain.UserStat}.
 */
@RestController
@RequestMapping("/api")
public class UserStatResource {

    private final Logger log = LoggerFactory.getLogger(UserStatResource.class);

    private static final String ENTITY_NAME = "userStat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserStatService userStatService;

    private final UserStatQueryService userStatQueryService;

    public UserStatResource(UserStatService userStatService, UserStatQueryService userStatQueryService) {
        this.userStatService = userStatService;
        this.userStatQueryService = userStatQueryService;
    }

    /**
     * {@code POST  /user-stats} : Create a new userStat.
     *
     * @param userStat the userStat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userStat, or with status {@code 400 (Bad Request)} if the userStat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-stats")
    public ResponseEntity<UserStat> createUserStat(@RequestBody UserStat userStat) throws URISyntaxException {
        log.debug("REST request to save UserStat : {}", userStat);
        if (userStat.getId() != null) {
            throw new BadRequestAlertException("A new userStat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserStat result = userStatService.save(userStat);
        return ResponseEntity.created(new URI("/api/user-stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-stats} : Updates an existing userStat.
     *
     * @param userStat the userStat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userStat,
     * or with status {@code 400 (Bad Request)} if the userStat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userStat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-stats")
    public ResponseEntity<UserStat> updateUserStat(@RequestBody UserStat userStat) throws URISyntaxException {
        log.debug("REST request to update UserStat : {}", userStat);
        if (userStat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserStat result = userStatService.save(userStat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userStat.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-stats} : get all the userStats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userStats in body.
     */
    @GetMapping("/user-stats")
    public ResponseEntity<List<UserStat>> getAllUserStats(UserStatCriteria criteria) {
        log.debug("REST request to get UserStats by criteria: {}", criteria);
        List<UserStat> entityList = userStatQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /user-stats/count} : count all the userStats.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/user-stats/count")
    public ResponseEntity<Long> countUserStats(UserStatCriteria criteria) {
        log.debug("REST request to count UserStats by criteria: {}", criteria);
        return ResponseEntity.ok().body(userStatQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-stats/:id} : get the "id" userStat.
     *
     * @param id the id of the userStat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userStat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-stats/{id}")
    public ResponseEntity<UserStat> getUserStat(@PathVariable Long id) {
        log.debug("REST request to get UserStat : {}", id);
        Optional<UserStat> userStat = userStatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userStat);
    }

    /**
     * {@code DELETE  /user-stats/:id} : delete the "id" userStat.
     *
     * @param id the id of the userStat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-stats/{id}")
    public ResponseEntity<Void> deleteUserStat(@PathVariable Long id) {
        log.debug("REST request to delete UserStat : {}", id);
        userStatService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
