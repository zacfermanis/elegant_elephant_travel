package com.elegantelephant.service;

import com.elegantelephant.domain.QuoteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing QuoteRequest.
 */
public interface QuoteRequestService {

    /**
     * Save a quoteRequest.
     *
     * @param quoteRequest the entity to save
     * @return the persisted entity
     */
    QuoteRequest save(QuoteRequest quoteRequest);

    /**
     *  Get all the quoteRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QuoteRequest> findAll(Pageable pageable);

    /**
     *  Get the "id" quoteRequest.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QuoteRequest findOne(Long id);

    /**
     *  Delete the "id" quoteRequest.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
