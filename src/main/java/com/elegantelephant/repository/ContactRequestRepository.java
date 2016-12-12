package com.elegantelephant.repository;

import com.elegantelephant.domain.ContactRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ContactRequest entity.
 */
@SuppressWarnings("unused")
public interface ContactRequestRepository extends JpaRepository<ContactRequest,Long> {

}
