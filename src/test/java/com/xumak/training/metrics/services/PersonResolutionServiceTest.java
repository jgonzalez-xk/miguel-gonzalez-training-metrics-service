package com.xumak.training.metrics.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.xumak.training.metrics.controllers.PersonResolutionController;
import com.xumak.training.metrics.data.PersonResolutionRepository;
import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.impl.PersonResolutionServiceImpl;
import com.xumak.training.metrics.utils.PersonResolutionModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class PersonResolutionServiceTest {

        // MOCK VARIABLES
        PersonResolution personResolution;
        EntityModel<PersonResolution> entityModel;

        @InjectMocks
        PersonResolutionServiceImpl service;
        @Mock
        PersonResolutionRepository repo;
        @Mock
        PersonResolutionModelAssembler assembler;

        @BeforeEach
        void setup() {
                // Mock PersonResolution Object
                personResolution = new PersonResolution(0, 0, 0, 0, "www.google.com");
                // Mock PersonResolution ModelAssembler
                entityModel = EntityModel.of(personResolution,
                                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId()))
                                                .withSelfRel());
                // Mock PersonResolution Assempler
                Mockito.when(assembler.toModel(personResolution)).thenReturn(entityModel);
        }

        @Test
        void findById() {
                // Mock PersonResolutionRepository findById
                Mockito.when(repo.findById(personResolution.getId()))
                                .thenReturn(Optional.of(personResolution));

                assertEquals(service.findById(personResolution.getId()), entityModel);
        }

        @Test
        void findBetweenDates() {
                Date start_date = Date.from(ZonedDateTime.now().minusMonths(5).toInstant());
                Date end_date = new Date();

                // List for the mocked repo
                List<PersonResolution> newList = new ArrayList<>();
                newList.add(personResolution);

                // List for the expected result
                List<EntityModel<PersonResolution>> personResolutions = new ArrayList<>();
                personResolutions.add(entityModel);

                // Expected result
                CollectionModel<EntityModel<PersonResolution>> collectionModel = CollectionModel.of(personResolutions,
                                linkTo(methodOn(PersonResolutionController.class).all(start_date,
                                                end_date)).withSelfRel());

                // Mock PersonResolutionRepository findByCreatedAtBetween
                Mockito.when(repo.findByCreatedAtBetween(start_date, end_date)).thenReturn(newList);

                assertEquals(service.findBetweenDates(start_date, end_date), collectionModel);
        }

}
