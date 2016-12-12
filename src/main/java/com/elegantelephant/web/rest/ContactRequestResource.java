package com.elegantelephant.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.elegantelephant.domain.ContactRequest;
import com.elegantelephant.service.ContactRequestService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ContactRequest.
 */
@RestController
@RequestMapping("/api")
public class ContactRequestResource {

    private final Logger log = LoggerFactory.getLogger(ContactRequestResource.class);

    @Inject
    private ContactRequestService contactRequestService;

    /**
     * POST  /contact-requests : Create a new contactRequest.
     *
     * @param contactRequest the contactRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactRequest, or with status 400 (Bad Request) if the contactRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-requests")
    @Timed
    public ResponseEntity<ContactRequest> createContactRequest(@Valid @RequestBody ContactRequest contactRequest) throws URISyntaxException {
        log.debug("REST request to save ContactRequest : {}", contactRequest);
        if (contactRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contactRequest", "idexists", "A new contactRequest cannot already have an ID")).body(null);
        }
        // Time stamp that sucka
        contactRequest.setRequestDate(ZonedDateTime.now());
        ContactRequest result = contactRequestService.save(contactRequest);
        return ResponseEntity.created(new URI("/api/contact-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contactRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contact-requests : Updates an existing contactRequest.
     *
     * @param contactRequest the contactRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactRequest,
     * or with status 400 (Bad Request) if the contactRequest is not valid,
     * or with status 500 (Internal Server Error) if the contactRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contact-requests")
    @Timed
    public ResponseEntity<ContactRequest> updateContactRequest(@Valid @RequestBody ContactRequest contactRequest) throws URISyntaxException {
        log.debug("REST request to update ContactRequest : {}", contactRequest);
        if (contactRequest.getId() == null) {
            return createContactRequest(contactRequest);
        }
        ContactRequest result = contactRequestService.save(contactRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contactRequest", contactRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contact-requests : get all the contactRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactRequests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/contact-requests")
    @Timed
    public ResponseEntity<List<ContactRequest>> getAllContactRequests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ContactRequests");
        Page<ContactRequest> page = contactRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contact-requests/:id : get the "id" contactRequest.
     *
     * @param id the id of the contactRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactRequest, or with status 404 (Not Found)
     */
    @GetMapping("/contact-requests/{id}")
    @Timed
    public ResponseEntity<ContactRequest> getContactRequest(@PathVariable Long id) {
        log.debug("REST request to get ContactRequest : {}", id);
        ContactRequest contactRequest = contactRequestService.findOne(id);
        return Optional.ofNullable(contactRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contact-requests/:id : delete the "id" contactRequest.
     *
     * @param id the id of the contactRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contact-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactRequest(@PathVariable Long id) {
        log.debug("REST request to delete ContactRequest : {}", id);
        contactRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contactRequest", id.toString())).build();
    }

}
