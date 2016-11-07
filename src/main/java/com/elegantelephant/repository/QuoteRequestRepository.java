package com.elegantelephant.repository;

import com.elegantelephant.domain.QuoteRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the QuoteRequest entity.
 */
@SuppressWarnings("unused")
public interface QuoteRequestRepository extends JpaRepository<QuoteRequest,Long> {

}
