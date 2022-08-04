package com.xumak.training.metrics.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.PersonResolutionService;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PersonResolutionController {

    private PersonResolutionService personResolutionService;

    @GetMapping("/metrics/person-resolution-metric/{id}")
    public EntityModel<PersonResolution> one(@PathVariable int id) {
        return personResolutionService.findById(id);
    }

    @GetMapping("/metrics/person-resolution-metric")
    public CollectionModel<EntityModel<PersonResolution>> all(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date) {
        return personResolutionService.findBetweenDates(start_date, end_date);
    }

    @PostMapping(value = "/metrics/person-resolution-metric")
    ResponseEntity<?> newPersonResolutionMetric(@RequestBody PersonResolution newPersonResolution) {
        return personResolutionService.newPersonResolution(newPersonResolution);
    }

}
