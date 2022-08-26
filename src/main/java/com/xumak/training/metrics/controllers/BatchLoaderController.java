package com.xumak.training.metrics.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.xumak.training.metrics.error.StateFalseException;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.services.impl.BatchLoaderServiceImpl;

@RestController()
@RequestMapping("/metrics/batch-loader-metric")
public class BatchLoaderController {

    @Autowired
    private BatchLoaderServiceImpl batchLoaderService;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BatchLoader>> one(@PathVariable int id) {
        EntityModel<BatchLoader> entityModel = batchLoaderService.findById(id);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<BatchLoader>>> all(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date) {
        CollectionModel<EntityModel<BatchLoader>> collectionModel = batchLoaderService.findBetweenDates(start_date,
                end_date);
        return ResponseEntity.created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(collectionModel);
    }

    @PostMapping()
    ResponseEntity<String> newBatchLoader(@RequestBody BatchLoader newBatchLoader) {
        try {
            EntityModel<BatchLoader> entityModel = batchLoaderService.newBatchLoader(newBatchLoader);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body("{\"state\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

    @ExceptionHandler(StateFalseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNoSuchElementFoundException(
            StateFalseException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
