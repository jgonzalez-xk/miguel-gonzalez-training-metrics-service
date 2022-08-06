package com.xumak.training.metrics.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

        @InjectMocks
        PersonResolutionServiceImpl service;
        @Mock
        PersonResolutionRepository repo;
        @Mock
        PersonResolutionModelAssembler assembler;

        @Test
        void findById() {
                PersonResolution personResolution = new PersonResolution(0, 0, 0, 0, "www.google.com");

                // Expected result
                EntityModel<PersonResolution> entityModel = EntityModel.of(personResolution,
                                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId()))
                                                .withSelfRel());

                // Mock methods used in service
                Mockito.when(repo.findById(personResolution.getId()))
                                .thenReturn(Optional.of(personResolution));
                Mockito.when(assembler.toModel(personResolution)).thenReturn(entityModel);

                assertEquals(service.findById(personResolution.getId()), entityModel);
        }

        @Test
        void findBetweenDates() {
                Date start_date = Date.from(ZonedDateTime.now().minusMonths(5).toInstant());
                Date end_date = new Date();
                List<PersonResolution> newList = new ArrayList<>();
                PersonResolution personResolution = new PersonResolution(0, 0, 0, 0, "www.google.com");
                newList.add(personResolution);

                // Mocking assembler's logic for expected result
                List<EntityModel<PersonResolution>> personResolutions = new ArrayList<>();
                EntityModel<PersonResolution> entityModel = EntityModel.of(personResolution,
                                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId()))
                                                .withSelfRel());
                personResolutions.add(entityModel);

                // Expected result
                CollectionModel<EntityModel<PersonResolution>> collectionModel = CollectionModel.of(personResolutions,
                                linkTo(methodOn(PersonResolutionController.class).all(start_date,
                                                end_date)).withSelfRel());

                // Mock methods used in service
                Mockito.when(repo.findByCreatedAtBetween(start_date, end_date)).thenReturn(newList);
                Mockito.when(assembler.toModel(personResolution)).thenReturn(entityModel);

                assertEquals(service.findBetweenDates(start_date, end_date), collectionModel);
        }

}
