package com.xumak.training.metrics.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.xumak.training.metrics.data.PersonResolutionRepository;
import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.utils.PersonResolutionModelAssembler;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PersonResolutionController {
    private final PersonResolutionRepository repository;

    private final PersonResolutionModelAssembler assembler;

    PersonResolutionController(PersonResolutionRepository repository, PersonResolutionModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/metrics/person-resolution-metrics")
    public CollectionModel<EntityModel<PersonResolution>> all() {

        List<EntityModel<PersonResolution>> personResolutions = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(personResolutions,
                linkTo(methodOn(PersonResolutionController.class).all()).withSelfRel());
    }

    @PostMapping(value = "/metrics/person-resolution-metric")
    ResponseEntity<?> newPersonResolutionMetric(@RequestBody PersonResolution newPersonResolution) {
        EntityModel<PersonResolution> entityModel = assembler.toModel(repository.save(newPersonResolution));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

}
