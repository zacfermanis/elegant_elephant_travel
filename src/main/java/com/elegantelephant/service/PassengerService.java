package com.elegantelephant.service;

import com.elegantelephant.domain.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Passenger.
 */
public interface PassengerService {

    /**
     * Save a passenger.
     *
     * @param passenger the entity to save
     * @return the persisted entity
     */
    Passenger save(Passenger passenger);

    /**
     *  Get all the passengers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Passenger> findAll(Pageable pageable);

    /**
     *  Get the "id" passenger.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Passenger findOne(Long id);

    /**
     *  Delete the "id" passenger.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
