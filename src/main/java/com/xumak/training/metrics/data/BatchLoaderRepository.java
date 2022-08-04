package com.xumak.training.metrics.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xumak.training.metrics.models.BatchLoader;

public interface BatchLoaderRepository extends JpaRepository<BatchLoader, Integer> {

    List<BatchLoader> findByCreatedAtBetween(Date start, Date end);
}
