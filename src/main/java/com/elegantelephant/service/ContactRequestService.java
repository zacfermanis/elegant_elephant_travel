package com.elegantelephant.service;

import com.elegantelephant.domain.ContactRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ContactRequest.
 */
public interface ContactRequestService {

    /**
     * Save a contactRequest.
     *
     * @param contactRequest the entity to save
     * @return the persisted entity
     */
    ContactRequest save(ContactRequest contactRequest);

    /**
     *  Get all the contactRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ContactRequest> findAll(Pageable pageable);

    /**
     *  Get the "id" contactRequest.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ContactRequest findOne(Long id);

    /**
     *  Delete the "id" contactRequest.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
