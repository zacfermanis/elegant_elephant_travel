package com.elegantelephant.service.impl;

import com.elegantelephant.service.QuoteRequestService;
import com.elegantelephant.domain.QuoteRequest;
import com.elegantelephant.repository.QuoteRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing QuoteRequest.
 */
@Service
@Transactional
public class QuoteRequestServiceImpl implements QuoteRequestService{

    private final Logger log = LoggerFactory.getLogger(QuoteRequestServiceImpl.class);
    
    @Inject
    private QuoteRequestRepository quoteRequestRepository;

    /**
     * Save a quoteRequest.
     *
     * @param quoteRequest the entity to save
     * @return the persisted entity
     */
    public QuoteRequest save(QuoteRequest quoteRequest) {
        log.debug("Request to save QuoteRequest : {}", quoteRequest);
        QuoteRequest result = quoteRequestRepository.save(quoteRequest);
        return result;
    }

    /**
     *  Get all the quoteRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<QuoteRequest> findAll(Pageable pageable) {
        log.debug("Request to get all QuoteRequests");
        Page<QuoteRequest> result = quoteRequestRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one quoteRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public QuoteRequest findOne(Long id) {
        log.debug("Request to get QuoteRequest : {}", id);
        QuoteRequest quoteRequest = quoteRequestRepository.findOne(id);
        return quoteRequest;
    }

    /**
     *  Delete the  quoteRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QuoteRequest : {}", id);
        quoteRequestRepository.delete(id);
    }
}
