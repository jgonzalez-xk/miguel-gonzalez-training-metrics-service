package com.xumak.training.metrics.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.impl.PersonResolutionServiceImpl;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PersonResolutionController {

    @Autowired
    private PersonResolutionServiceImpl personResolutionService;

    @GetMapping("/metrics/person-resolution-metric/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {
        try {
            EntityModel<PersonResolution> entityModel = personResolutionService.findById(id);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }

    }

    @GetMapping("/metrics/person-resolution-metric")
    public ResponseEntity<?> all(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date) {
        try {
            CollectionModel<EntityModel<PersonResolution>> collectionModel = personResolutionService
                    .findBetweenDates(start_date, end_date);
            return ResponseEntity.created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(collectionModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

    @PostMapping(value = "/metrics/person-resolution-metric")
    ResponseEntity<?> newPersonResolutionMetric(@RequestBody PersonResolution newPersonResolution) {
        try {
            EntityModel<PersonResolution> entityModel = personResolutionService
                    .newPersonResolution(newPersonResolution);
            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body("{\"state\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

}
