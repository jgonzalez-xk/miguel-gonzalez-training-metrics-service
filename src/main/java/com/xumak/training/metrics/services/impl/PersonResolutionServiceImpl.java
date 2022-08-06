package com.xumak.training.metrics.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.xumak.training.metrics.controllers.PersonResolutionController;
import com.xumak.training.metrics.data.PersonResolutionRepository;
import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.PersonResolutionService;
import com.xumak.training.metrics.utils.PersonResolutionModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonResolutionServiceImpl implements PersonResolutionService {
    private final PersonResolutionRepository repository;
    private final PersonResolutionModelAssembler assembler;

    PersonResolutionServiceImpl(PersonResolutionRepository repository, PersonResolutionModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    public EntityModel<PersonResolution> findById(int id) {
        PersonResolution personResolution = repository.findById(id).orElseThrow(() -> new RuntimeException());
        return assembler.toModel(personResolution);
    }

    public CollectionModel<EntityModel<PersonResolution>> findBetweenDates(Date start_date, Date end_date) {
        List<EntityModel<PersonResolution>> personResolutions = repository
                .findByCreatedAtBetween(start_date, end_date)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(personResolutions,
                linkTo(methodOn(PersonResolutionController.class).all(start_date,
                        end_date)).withSelfRel());
    }

    public EntityModel<PersonResolution> newPersonResolution(PersonResolution newPersonResolution) {
        return assembler.toModel(repository.save(newPersonResolution));
    }
}
