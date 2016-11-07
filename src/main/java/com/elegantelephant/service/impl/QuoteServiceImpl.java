package com.elegantelephant.service.impl;

import com.elegantelephant.service.QuoteService;
import com.elegantelephant.domain.Quote;
import com.elegantelephant.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Quote.
 */
@Service
@Transactional
public class QuoteServiceImpl implements QuoteService{

    private final Logger log = LoggerFactory.getLogger(QuoteServiceImpl.class);
    
    @Inject
    private QuoteRepository quoteRepository;

    /**
     * Save a quote.
     *
     * @param quote the entity to save
     * @return the persisted entity
     */
    public Quote save(Quote quote) {
        log.debug("Request to save Quote : {}", quote);
        Quote result = quoteRepository.save(quote);
        return result;
    }

    /**
     *  Get all the quotes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Quote> findAll(Pageable pageable) {
        log.debug("Request to get all Quotes");
        Page<Quote> result = quoteRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one quote by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Quote findOne(Long id) {
        log.debug("Request to get Quote : {}", id);
        Quote quote = quoteRepository.findOne(id);
        return quote;
    }

    /**
     *  Delete the  quote by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Quote : {}", id);
        quoteRepository.delete(id);
    }
}
