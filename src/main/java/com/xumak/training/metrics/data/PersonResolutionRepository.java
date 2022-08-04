package com.xumak.training.metrics.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xumak.training.metrics.models.PersonResolution;

public interface PersonResolutionRepository extends JpaRepository<PersonResolution, Integer> {
    List<PersonResolution> findByCreatedAtBetween(Date start, Date end);
}
