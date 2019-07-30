package com.hongz.uneed.web.rest;

import com.hongz.uneed.service.UserJobQueryService;
import com.hongz.uneed.service.UserJobService;
import com.hongz.uneed.service.dto.UserJobCriteria;
import com.hongz.uneed.service.dto.UserJobDTO;
import com.hongz.uneed.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hongz.uneed.domain.UserJob}.
 */
@RestController
@RequestMapping("/api")
public class UserJobResource {

    private final Logger log = LoggerFactory.getLogger(UserJobResource.class);

    private static final String ENTITY_NAME = "userJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserJobService userJobService;

    private final UserJobQueryService userJobQueryService;

    public UserJobResource(UserJobService userJobService, UserJobQueryService userJobQueryService) {
        this.userJobService = userJobService;
        this.userJobQueryService = userJobQueryService;
    }

    /**
     * {@code POST  /user-jobs} : Create a new userJob.
     *
     * @param userJobDTO the userJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userJobDTO, or with status {@code 400 (Bad Request)} if the userJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-jobs")
    public ResponseEntity<UserJobDTO> createUserJob(@Valid @RequestBody UserJobDTO userJobDTO) throws URISyntaxException {
        log.debug("REST request to save UserJob : {}", userJobDTO);
        if (userJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new userJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserJobDTO result = userJobService.save(userJobDTO);
        return ResponseEntity.created(new URI("/api/user-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-jobs} : Updates an existing userJob.
     *
     * @param userJobDTO the userJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userJobDTO,
     * or with status {@code 400 (Bad Request)} if the userJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-jobs")
    public ResponseEntity<UserJobDTO> updateUserJob(@Valid @RequestBody UserJobDTO userJobDTO) throws URISyntaxException {
        log.debug("REST request to update UserJob : {}", userJobDTO);
        if (userJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserJobDTO result = userJobService.save(userJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userJobDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-jobs/current} : get userJobs by current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-jobs/current")
    public ResponseEntity<UserJobDTO> getCurrentUserJobs() {
        log.debug("REST request to get UserJobs by currency user");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable( userJobService.findByCurrentUser()));
    }

    /**
     * {@code GET  /user-jobs} : get all the userJobs.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userJobs in body.
     */
    @GetMapping("/user-jobs")
    public ResponseEntity<List<UserJobDTO>> getAllUserJobs(UserJobCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get UserJobs by criteria: {}", criteria);
        Page<UserJobDTO> page = userJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /user-jobs/count} : count all the userJobs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/user-jobs/count")
    public ResponseEntity<Long> countUserJobs(UserJobCriteria criteria) {
        log.debug("REST request to count UserJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(userJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-jobs/:id} : get the "id" userJob.
     *
     * @param id the id of the userJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-jobs/{id}")
    public ResponseEntity<UserJobDTO> getUserJob(@PathVariable Long id) {
        log.debug("REST request to get UserJob : {}", id);
        Optional<UserJobDTO> userJobDTO = userJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userJobDTO);
    }

    /**
     * {@code DELETE  /user-jobs/:id} : delete the "id" userJob.
     *
     * @param id the id of the userJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-jobs/{id}")
    public ResponseEntity<Void> deleteUserJob(@PathVariable Long id) {
        log.debug("REST request to delete UserJob : {}", id);
        userJobService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
