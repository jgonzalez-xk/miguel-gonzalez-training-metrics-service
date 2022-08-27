package com.xumak.training.metrics.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.impl.PersonResolutionServiceImpl;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PersonResolutionControllerTest {

    private String uri = "/metrics/person-resolution-metric/";

    // MOCK VARIABLES
    private PersonResolution personResolution;
    private EntityModel<PersonResolution> entityModel;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    PersonResolutionController controller;

    @Mock
    PersonResolutionServiceImpl service;

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
    public void testAddPersonResolution() throws Exception {
        // Mock PersonResolutionService
        doReturn(entityModel).when(service).newPersonResolution(any(PersonResolution.class));
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personResolution))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().string(containsString("{\"state\": true}")))
                .andExpect(status().isCreated());
    }

    @Test
    public void testFailToAddPersonResolution() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(null))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}