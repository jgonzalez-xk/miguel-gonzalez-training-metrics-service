package com.xumak.training.metrics.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.xumak.training.metrics.controllers.PersonResolutionController;
import com.xumak.training.metrics.data.PersonResolutionRepository;
import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.impl.PersonResolutionServiceImpl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PersonResolutionServiceTest {

        private String uri = "/metrics/person-resolution-metric";

        // MOCK VARIABLES
        private PersonResolution personResolution;
        private EntityModel<PersonResolution> entityModel;

        @Autowired
        private MockMvc mockMvc;

        @InjectMocks
        PersonResolutionController controller;

        @Mock
        PersonResolutionServiceImpl service;

        @Mock
        PersonResolutionRepository repo;

        @BeforeEach
        void setup() {
                controller = new PersonResolutionController();
                mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
                MockitoAnnotations.initMocks(this);

                // Mock PersonResolution Object
                personResolution = new PersonResolution(0, 0, 0, 0, "www.google.com");
                // Mock PersonResolutionModelAssembler
                entityModel = EntityModel.of(personResolution,
                                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId()))
                                                .withSelfRel());
        }

        @Test
        void findById() throws Exception {
                doReturn(entityModel).when(service).findById(any(Integer.class));
                this.mockMvc.perform(MockMvcRequestBuilders
                                .get(uri + "/" + personResolution.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(personResolution.getId())))
                                .andExpect(jsonPath("$.endpoint", is(personResolution.getEndpoint())));
                ;
        }

        @Test
        void findBetweenDates() throws Exception {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date start_date = Date.from(ZonedDateTime.now().minusMonths(5).toInstant());
                Date end_date = new Date();
                String string_start_date = dateFormat.format(start_date);
                String string_end_date = dateFormat.format(end_date);

                // Mock second PersonResolution Object and entityModel
                PersonResolution personResolution2 = new PersonResolution(0, 0, 0, 0, "www.mycompany.com");
                EntityModel<PersonResolution> entityModel2 = EntityModel.of(personResolution2,
                                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId()))
                                                .withSelfRel());

                // List for the expected result
                List<EntityModel<PersonResolution>> personResolutions = new ArrayList<>();
                personResolutions.add(entityModel);
                personResolutions.add(entityModel2);

                // Expected result
                CollectionModel<EntityModel<PersonResolution>> collectionModel = CollectionModel.of(personResolutions,
                                linkTo(methodOn(PersonResolutionController.class).all(start_date,
                                                end_date)).withSelfRel());

                doReturn(collectionModel).when(service).findBetweenDates(any(Date.class), any(Date.class));

                this.mockMvc.perform(MockMvcRequestBuilders
                                .get(uri)
                                .param("start_date", string_start_date)
                                .param("end_date", string_end_date)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(2)));
        }

}
