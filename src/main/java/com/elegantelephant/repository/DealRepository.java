package com.elegantelephant.repository;

import com.elegantelephant.domain.Deal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Deal entity.
 */
@SuppressWarnings("unused")
public interface DealRepository extends JpaRepository<Deal,Long> {

}
