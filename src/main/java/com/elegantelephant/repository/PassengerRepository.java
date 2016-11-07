package com.elegantelephant.repository;

import com.elegantelephant.domain.Passenger;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Passenger entity.
 */
@SuppressWarnings("unused")
public interface PassengerRepository extends JpaRepository<Passenger,Long> {

}
