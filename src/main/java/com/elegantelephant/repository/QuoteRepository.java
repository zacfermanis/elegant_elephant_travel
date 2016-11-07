package com.elegantelephant.repository;

import com.elegantelephant.domain.Quote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Quote entity.
 */
@SuppressWarnings("unused")
public interface QuoteRepository extends JpaRepository<Quote,Long> {

}
