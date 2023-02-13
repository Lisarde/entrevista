package com.virtualcave.exercise.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virtualcave.exercise.entity.TRates;

@Repository
public interface TRatesRepository extends CrudRepository<TRates, Integer> {

	Optional<TRates> findByBrandIdAndProductIdAndStartDate(@Param("brandId") Integer brandId,
			@Param("productId") Integer productId, @Param("startDate") LocalDate startDate);

	Optional<TRates> findByBrandIdAndProductIdAndEndDate(@Param("brandId") Integer brandId,
			@Param("productId") Integer productId, @Param("endDate") LocalDate endDate);

	Optional<TRates> findByBrandIdAndProductIdAndStartDateAndEndDate(@Param("brandId") Integer brandId,
			@Param("productId") Integer productId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
}
