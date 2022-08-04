package com.xumak.training.metrics.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.xumak.training.metrics.data.PersonResolutionRepository;
import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.utils.PersonResolutionModelAssembler;

import java.util.stream.Collectors;

import java.util.Date;
import java.util.List;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonResolutionController {

    private final PersonResolutionRepository repository;
    private final PersonResolutionModelAssembler assembler;

    PersonResolutionController(PersonResolutionRepository repository, PersonResolutionModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/metrics/person-resolution-metric/{id}")
    public EntityModel<PersonResolution> one(@PathVariable int id) {

        PersonResolution personResolution = repository.findById(id).orElseThrow(() -> new RuntimeException());
        return assembler.toModel(personResolution);
    }

    @GetMapping("/metrics/person-resolution-metric")
    public CollectionModel<EntityModel<PersonResolution>> all(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date) {

        List<EntityModel<PersonResolution>> personResolutions = repository.findByCreatedAtBetween(start_date, end_date)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(personResolutions,
                linkTo(methodOn(PersonResolutionController.class).all(start_date,
                        end_date)).withSelfRel());
    }

    @PostMapping(value = "/metrics/person-resolution-metric")
    ResponseEntity<?> newPersonResolutionMetric(@RequestBody PersonResolution newPersonResolution) {

        try {
            EntityModel<PersonResolution> entityModel = assembler.toModel(repository.save(newPersonResolution));
            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body("{\"state\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

}
