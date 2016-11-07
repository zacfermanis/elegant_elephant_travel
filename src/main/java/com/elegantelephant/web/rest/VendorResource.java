package com.elegantelephant.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.elegantelephant.domain.Vendor;
import com.elegantelephant.service.VendorService;
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
 * REST controller for managing Vendor.
 */
@RestController
@RequestMapping("/api")
public class VendorResource {

    private final Logger log = LoggerFactory.getLogger(VendorResource.class);
        
    @Inject
    private VendorService vendorService;

    /**
     * POST  /vendors : Create a new vendor.
     *
     * @param vendor the vendor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vendor, or with status 400 (Bad Request) if the vendor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vendors")
    @Timed
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) throws URISyntaxException {
        log.debug("REST request to save Vendor : {}", vendor);
        if (vendor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vendor", "idexists", "A new vendor cannot already have an ID")).body(null);
        }
        Vendor result = vendorService.save(vendor);
        return ResponseEntity.created(new URI("/api/vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vendor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vendors : Updates an existing vendor.
     *
     * @param vendor the vendor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vendor,
     * or with status 400 (Bad Request) if the vendor is not valid,
     * or with status 500 (Internal Server Error) if the vendor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vendors")
    @Timed
    public ResponseEntity<Vendor> updateVendor(@RequestBody Vendor vendor) throws URISyntaxException {
        log.debug("REST request to update Vendor : {}", vendor);
        if (vendor.getId() == null) {
            return createVendor(vendor);
        }
        Vendor result = vendorService.save(vendor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vendor", vendor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vendors : get all the vendors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vendors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/vendors")
    @Timed
    public ResponseEntity<List<Vendor>> getAllVendors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Vendors");
        Page<Vendor> page = vendorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vendors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vendors/:id : get the "id" vendor.
     *
     * @param id the id of the vendor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vendor, or with status 404 (Not Found)
     */
    @GetMapping("/vendors/{id}")
    @Timed
    public ResponseEntity<Vendor> getVendor(@PathVariable Long id) {
        log.debug("REST request to get Vendor : {}", id);
        Vendor vendor = vendorService.findOne(id);
        return Optional.ofNullable(vendor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vendors/:id : delete the "id" vendor.
     *
     * @param id the id of the vendor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vendors/{id}")
    @Timed
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        log.debug("REST request to delete Vendor : {}", id);
        vendorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vendor", id.toString())).build();
    }

}
