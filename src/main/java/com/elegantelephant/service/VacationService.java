package com.elegantelephant.service;

import com.elegantelephant.domain.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Vacation.
 */
public interface VacationService {

    /**
     * Save a vacation.
     *
     * @param vacation the entity to save
     * @return the persisted entity
     */
    Vacation save(Vacation vacation);

    /**
     *  Get all the vacations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Vacation> findAll(Pageable pageable);

    /**
     *  Get the "id" vacation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Vacation findOne(Long id);

    /**
     *  Delete the "id" vacation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
