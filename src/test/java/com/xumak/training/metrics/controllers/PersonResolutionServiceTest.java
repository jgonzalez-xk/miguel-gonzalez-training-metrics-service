package com.xumak.training.metrics.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;

import com.xumak.training.metrics.data.PersonResolutionRepository;
import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.impl.PersonResolutionServiceImpl;
import com.xumak.training.metrics.utils.PersonResolutionModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class PersonResolutionServiceTest {

    @InjectMocks
    PersonResolutionServiceImpl personResolutionServiceImpl;

    @Mock
    PersonResolutionRepository personResolutionRepository;

    @Mock
    PersonResolutionModelAssembler personResolutionModelAssembler;

    @Test
    void findById() {
        // Mock PersonResolutionRepository
        PersonResolution personResolution = new PersonResolution(0, 0, 0, 0, "www.google.com");
        Mockito.when(personResolutionRepository.findById(personResolution.getId()))
                .thenReturn(Optional.of(personResolution));
        // Mock PersonResolutionModelAssembler
        EntityModel<PersonResolution> entityModel = EntityModel.of(personResolution,
                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId())).withSelfRel());
        Mockito.when(personResolutionModelAssembler.toModel(personResolution)).thenReturn(entityModel);

        // Assert PersonResolutionServiceImpl return the correct EntityModel
        assertEquals(personResolutionServiceImpl.findById(personResolution.getId()), entityModel);
    }

}
