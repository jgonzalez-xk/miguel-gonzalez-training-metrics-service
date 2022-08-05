package com.xumak.training.metrics.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.services.impl.BatchLoaderServiceImpl;

@RestController
public class BatchLoaderController {

    @Autowired
    private BatchLoaderServiceImpl batchLoaderService;

    @GetMapping("/metrics/batch-loader-metric/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {
        try {
            EntityModel<BatchLoader> entityModel = batchLoaderService.findById(id);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

    @GetMapping("/metrics/batch-loader-metric")
    public ResponseEntity<?> all(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date) {
        try {
            CollectionModel<EntityModel<BatchLoader>> collectionModel = batchLoaderService.findBetweenDates(start_date,
                    end_date);
            return ResponseEntity.created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(collectionModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }

    }

    @PostMapping("/metrics/batch-loader-metric")
    ResponseEntity<?> newBatchLoaderMetric(@RequestBody BatchLoader newBatchLoader) {
        try {
            EntityModel<BatchLoader> entityModel = batchLoaderService.newBatchLoaderMetric(newBatchLoader);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body("{\"state\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

}
