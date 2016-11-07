package com.elegantelephant.service.impl;

import com.elegantelephant.service.VendorService;
import com.elegantelephant.domain.Vendor;
import com.elegantelephant.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Vendor.
 */
@Service
@Transactional
public class VendorServiceImpl implements VendorService{

    private final Logger log = LoggerFactory.getLogger(VendorServiceImpl.class);
    
    @Inject
    private VendorRepository vendorRepository;

    /**
     * Save a vendor.
     *
     * @param vendor the entity to save
     * @return the persisted entity
     */
    public Vendor save(Vendor vendor) {
        log.debug("Request to save Vendor : {}", vendor);
        Vendor result = vendorRepository.save(vendor);
        return result;
    }

    /**
     *  Get all the vendors.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Vendor> findAll(Pageable pageable) {
        log.debug("Request to get all Vendors");
        Page<Vendor> result = vendorRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one vendor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Vendor findOne(Long id) {
        log.debug("Request to get Vendor : {}", id);
        Vendor vendor = vendorRepository.findOne(id);
        return vendor;
    }

    /**
     *  Delete the  vendor by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vendor : {}", id);
        vendorRepository.delete(id);
    }
}
