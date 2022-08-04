package com.xumak.training.metrics.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xumak.training.metrics.models.BatchLoader;

public interface BatchLoaderRepository extends JpaRepository<BatchLoader, Integer> {

}
