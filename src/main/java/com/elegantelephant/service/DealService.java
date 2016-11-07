package com.elegantelephant.service;

import com.elegantelephant.domain.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Deal.
 */
public interface DealService {

    /**
     * Save a deal.
     *
     * @param deal the entity to save
     * @return the persisted entity
     */
    Deal save(Deal deal);

    /**
     *  Get all the deals.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Deal> findAll(Pageable pageable);

    /**
     *  Get the "id" deal.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Deal findOne(Long id);

    /**
     *  Delete the "id" deal.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
