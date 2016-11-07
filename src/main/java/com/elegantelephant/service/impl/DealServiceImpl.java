package com.elegantelephant.service.impl;

import com.elegantelephant.service.DealService;
import com.elegantelephant.domain.Deal;
import com.elegantelephant.repository.DealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Deal.
 */
@Service
@Transactional
public class DealServiceImpl implements DealService{

    private final Logger log = LoggerFactory.getLogger(DealServiceImpl.class);
    
    @Inject
    private DealRepository dealRepository;

    /**
     * Save a deal.
     *
     * @param deal the entity to save
     * @return the persisted entity
     */
    public Deal save(Deal deal) {
        log.debug("Request to save Deal : {}", deal);
        Deal result = dealRepository.save(deal);
        return result;
    }

    /**
     *  Get all the deals.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Deal> findAll(Pageable pageable) {
        log.debug("Request to get all Deals");
        Page<Deal> result = dealRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one deal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Deal findOne(Long id) {
        log.debug("Request to get Deal : {}", id);
        Deal deal = dealRepository.findOne(id);
        return deal;
    }

    /**
     *  Delete the  deal by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Deal : {}", id);
        dealRepository.delete(id);
    }
}
