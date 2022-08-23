package com.xumak.training.metrics.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.xumak.training.metrics.error.StateFalseException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/metrics/person-resolution-metric")
public class PersonResolutionController extends StateFalseException {

    @Autowired
    private PersonResolutionServiceImpl personResolutionService;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PersonResolution>> one(@PathVariable int id) {
        EntityModel<PersonResolution> entityModel = personResolutionService.findById(id);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<PersonResolution>>> all(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date) {
        CollectionModel<EntityModel<PersonResolution>> collectionModel = personResolutionService
                .findBetweenDates(start_date, end_date);
        return ResponseEntity.created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(collectionModel);
    }

    @PostMapping()
    ResponseEntity<String> newPersonResolution(@RequestBody PersonResolution newPersonResolution) {
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
