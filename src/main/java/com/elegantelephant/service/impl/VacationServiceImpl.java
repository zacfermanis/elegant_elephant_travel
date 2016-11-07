package com.elegantelephant.service.impl;

import com.elegantelephant.service.VacationService;
import com.elegantelephant.domain.Vacation;
import com.elegantelephant.repository.VacationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Vacation.
 */
@Service
@Transactional
public class VacationServiceImpl implements VacationService{

    private final Logger log = LoggerFactory.getLogger(VacationServiceImpl.class);
    
    @Inject
    private VacationRepository vacationRepository;

    /**
     * Save a vacation.
     *
     * @param vacation the entity to save
     * @return the persisted entity
     */
    public Vacation save(Vacation vacation) {
        log.debug("Request to save Vacation : {}", vacation);
        Vacation result = vacationRepository.save(vacation);
        return result;
    }

    /**
     *  Get all the vacations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Vacation> findAll(Pageable pageable) {
        log.debug("Request to get all Vacations");
        Page<Vacation> result = vacationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one vacation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Vacation findOne(Long id) {
        log.debug("Request to get Vacation : {}", id);
        Vacation vacation = vacationRepository.findOneWithEagerRelationships(id);
        return vacation;
    }

    /**
     *  Delete the  vacation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vacation : {}", id);
        vacationRepository.delete(id);
    }
}
