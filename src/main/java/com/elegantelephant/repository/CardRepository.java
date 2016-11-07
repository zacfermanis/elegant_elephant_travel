package com.elegantelephant.repository;

import com.elegantelephant.domain.Card;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Card entity.
 */
@SuppressWarnings("unused")
public interface CardRepository extends JpaRepository<Card,Long> {

}
