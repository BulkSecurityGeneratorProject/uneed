package com.hongz.uneed.service;

import com.hongz.uneed.domain.UserStat;
import com.hongz.uneed.repository.UserStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UserStat}.
 */
@Service
@Transactional
public class UserStatService {

    private final Logger log = LoggerFactory.getLogger(UserStatService.class);

    private final UserStatRepository userStatRepository;

    public UserStatService(UserStatRepository userStatRepository) {
        this.userStatRepository = userStatRepository;
    }

    /**
     * Save a userStat.
     *
     * @param userStat the entity to save.
     * @return the persisted entity.
     */
    public UserStat save(UserStat userStat) {
        log.debug("Request to save UserStat : {}", userStat);
        return userStatRepository.save(userStat);
    }

    /**
     * Get all the userStats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserStat> findAll() {
        log.debug("Request to get all UserStats");
        return userStatRepository.findAll();
    }


    /**
     * Get one userStat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserStat> findOne(Long id) {
        log.debug("Request to get UserStat : {}", id);
        return userStatRepository.findById(id);
    }

    /**
     * Delete the userStat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserStat : {}", id);
        userStatRepository.deleteById(id);
    }
}
