package com.elegantelephant.service.impl;

import com.elegantelephant.service.ContactRequestService;
import com.elegantelephant.domain.ContactRequest;
import com.elegantelephant.repository.ContactRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ContactRequest.
 */
@Service
@Transactional
public class ContactRequestServiceImpl implements ContactRequestService{

    private final Logger log = LoggerFactory.getLogger(ContactRequestServiceImpl.class);
    
    @Inject
    private ContactRequestRepository contactRequestRepository;

    /**
     * Save a contactRequest.
     *
     * @param contactRequest the entity to save
     * @return the persisted entity
     */
    public ContactRequest save(ContactRequest contactRequest) {
        log.debug("Request to save ContactRequest : {}", contactRequest);
        ContactRequest result = contactRequestRepository.save(contactRequest);
        return result;
    }

    /**
     *  Get all the contactRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ContactRequest> findAll(Pageable pageable) {
        log.debug("Request to get all ContactRequests");
        Page<ContactRequest> result = contactRequestRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one contactRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ContactRequest findOne(Long id) {
        log.debug("Request to get ContactRequest : {}", id);
        ContactRequest contactRequest = contactRequestRepository.findOne(id);
        return contactRequest;
    }

    /**
     *  Delete the  contactRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactRequest : {}", id);
        contactRequestRepository.delete(id);
    }
}
