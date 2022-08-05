package com.xumak.training.metrics.services;

import java.util.Date;

import org.springframework.http.ResponseEntity;

import com.xumak.training.metrics.models.BatchLoader;

public interface BatchLoaderService {

    public ResponseEntity<?> findById(int id);

    public ResponseEntity<?> findBetweenDates(Date start_date, Date end_date);

    public ResponseEntity<?> newBatchLoaderMetric(BatchLoader newBatchLoader);

}
