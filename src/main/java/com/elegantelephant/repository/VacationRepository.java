package com.elegantelephant.repository;

import com.elegantelephant.domain.Vacation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Vacation entity.
 */
@SuppressWarnings("unused")
public interface VacationRepository extends JpaRepository<Vacation,Long> {

    @Query("select distinct vacation from Vacation vacation left join fetch vacation.passengers")
    List<Vacation> findAllWithEagerRelationships();

    @Query("select vacation from Vacation vacation left join fetch vacation.passengers where vacation.id =:id")
    Vacation findOneWithEagerRelationships(@Param("id") Long id);

}
