package com.elegantelephant.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.elegantelephant.domain.QuoteRequest;
import com.elegantelephant.service.QuoteRequestService;
import com.elegantelephant.web.rest.util.HeaderUtil;
import com.elegantelephant.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QuoteRequest.
 */
@RestController
@RequestMapping("/api")
public class QuoteRequestResource {

    private final Logger log = LoggerFactory.getLogger(QuoteRequestResource.class);
        
    @Inject
    private QuoteRequestService quoteRequestService;

    /**
     * POST  /quote-requests : Create a new quoteRequest.
     *
     * @param quoteRequest the quoteRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quoteRequest, or with status 400 (Bad Request) if the quoteRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/quote-requests")
    @Timed
    public ResponseEntity<QuoteRequest> createQuoteRequest(@RequestBody QuoteRequest quoteRequest) throws URISyntaxException {
        log.debug("REST request to save QuoteRequest : {}", quoteRequest);
        if (quoteRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("quoteRequest", "idexists", "A new quoteRequest cannot already have an ID")).body(null);
        }
        QuoteRequest result = quoteRequestService.save(quoteRequest);
        return ResponseEntity.created(new URI("/api/quote-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("quoteRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quote-requests : Updates an existing quoteRequest.
     *
     * @param quoteRequest the quoteRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quoteRequest,
     * or with status 400 (Bad Request) if the quoteRequest is not valid,
     * or with status 500 (Internal Server Error) if the quoteRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/quote-requests")
    @Timed
    public ResponseEntity<QuoteRequest> updateQuoteRequest(@RequestBody QuoteRequest quoteRequest) throws URISyntaxException {
        log.debug("REST request to update QuoteRequest : {}", quoteRequest);
        if (quoteRequest.getId() == null) {
            return createQuoteRequest(quoteRequest);
        }
        QuoteRequest result = quoteRequestService.save(quoteRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("quoteRequest", quoteRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quote-requests : get all the quoteRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of quoteRequests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/quote-requests")
    @Timed
    public ResponseEntity<List<QuoteRequest>> getAllQuoteRequests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of QuoteRequests");
        Page<QuoteRequest> page = quoteRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quote-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /quote-requests/:id : get the "id" quoteRequest.
     *
     * @param id the id of the quoteRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quoteRequest, or with status 404 (Not Found)
     */
    @GetMapping("/quote-requests/{id}")
    @Timed
    public ResponseEntity<QuoteRequest> getQuoteRequest(@PathVariable Long id) {
        log.debug("REST request to get QuoteRequest : {}", id);
        QuoteRequest quoteRequest = quoteRequestService.findOne(id);
        return Optional.ofNullable(quoteRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /quote-requests/:id : delete the "id" quoteRequest.
     *
     * @param id the id of the quoteRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quote-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuoteRequest(@PathVariable Long id) {
        log.debug("REST request to delete QuoteRequest : {}", id);
        quoteRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("quoteRequest", id.toString())).build();
    }

}
