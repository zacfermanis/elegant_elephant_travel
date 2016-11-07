package com.elegantelephant.service.impl;

import com.elegantelephant.service.PassengerService;
import com.elegantelephant.domain.Passenger;
import com.elegantelephant.repository.PassengerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Passenger.
 */
@Service
@Transactional
public class PassengerServiceImpl implements PassengerService{

    private final Logger log = LoggerFactory.getLogger(PassengerServiceImpl.class);
    
    @Inject
    private PassengerRepository passengerRepository;

    /**
     * Save a passenger.
     *
     * @param passenger the entity to save
     * @return the persisted entity
     */
    public Passenger save(Passenger passenger) {
        log.debug("Request to save Passenger : {}", passenger);
        Passenger result = passengerRepository.save(passenger);
        return result;
    }

    /**
     *  Get all the passengers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Passenger> findAll(Pageable pageable) {
        log.debug("Request to get all Passengers");
        Page<Passenger> result = passengerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one passenger by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Passenger findOne(Long id) {
        log.debug("Request to get Passenger : {}", id);
        Passenger passenger = passengerRepository.findOne(id);
        return passenger;
    }

    /**
     *  Delete the  passenger by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Passenger : {}", id);
        passengerRepository.delete(id);
    }
}
