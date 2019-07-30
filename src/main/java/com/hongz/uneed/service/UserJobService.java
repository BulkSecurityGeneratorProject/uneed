package com.hongz.uneed.service;

import com.hongz.uneed.domain.UserJob;
import com.hongz.uneed.repository.UserJobRepository;
import com.hongz.uneed.service.dto.UserJobDTO;
import com.hongz.uneed.service.mapper.UserJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link UserJob}.
 */
@Service
@Transactional
public class UserJobService {

    private final Logger log = LoggerFactory.getLogger(UserJobService.class);

    private final UserJobRepository userJobRepository;

    private final UserJobMapper userJobMapper;

    public UserJobService(UserJobRepository userJobRepository, UserJobMapper userJobMapper) {
        this.userJobRepository = userJobRepository;
        this.userJobMapper = userJobMapper;
    }

    /**
     * Save a userJob.
     *
     * @param userJobDTO the entity to save.
     * @return the persisted entity.
     */
    public UserJobDTO save(UserJobDTO userJobDTO) {
        log.debug("Request to save UserJob : {}", userJobDTO);
        UserJob userJob = userJobMapper.toEntity(userJobDTO);
        userJob = userJobRepository.save(userJob);
        return userJobMapper.toDto(userJob);
    }

    /**
     * Find userJobs by current user
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserJobDTO> findByCurrentUser() {
        log.debug("Find UserJobs by current user");
        return userJobRepository.findByUserIsCurrentUser()
            .stream()
            .map(userJobMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get all the userJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserJobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserJobs");
        return userJobRepository.findAll(pageable)
            .map(userJobMapper::toDto);
    }

    /**
     * Get all the userJobs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserJobDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userJobRepository.findAllWithEagerRelationships(pageable).map(userJobMapper::toDto);
    }
    

    /**
     * Get one userJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserJobDTO> findOne(Long id) {
        log.debug("Request to get UserJob : {}", id);
        return userJobRepository.findOneWithEagerRelationships(id)
            .map(userJobMapper::toDto);
    }

    /**
     * Delete the userJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserJob : {}", id);
        userJobRepository.deleteById(id);
    }
}
