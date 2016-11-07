package com.elegantelephant.service;

import com.elegantelephant.domain.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Quote.
 */
public interface QuoteService {

    /**
     * Save a quote.
     *
     * @param quote the entity to save
     * @return the persisted entity
     */
    Quote save(Quote quote);

    /**
     *  Get all the quotes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Quote> findAll(Pageable pageable);

    /**
     *  Get the "id" quote.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Quote findOne(Long id);

    /**
     *  Delete the "id" quote.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
