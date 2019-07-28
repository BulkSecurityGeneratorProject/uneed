package com.hongz.uneed.web.rest;

import com.hongz.uneed.service.UserJobQueryService;
import com.hongz.uneed.service.UserJobService;
import com.hongz.uneed.service.dto.UserJobCriteria;
import com.hongz.uneed.service.dto.UserJobDTO;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hongz.uneed.domain.UserJob}.
 */
@RestController
@RequestMapping("/api/pub")
public class PublicUserJobResource {

    private final Logger log = LoggerFactory.getLogger(PublicUserJobResource.class);

    private static final String ENTITY_NAME = "userJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserJobService userJobService;

    private final UserJobQueryService userJobQueryService;

    public PublicUserJobResource(UserJobService userJobService, UserJobQueryService userJobQueryService) {
        this.userJobService = userJobService;
        this.userJobQueryService = userJobQueryService;
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
}
