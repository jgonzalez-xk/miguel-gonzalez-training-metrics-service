package com.xumak.training.metrics.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.xumak.training.metrics.models.PersonResolution;
import com.xumak.training.metrics.services.impl.PersonResolutionServiceImpl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class PersonResolutionControllerTest {

    // MOCK REQUEST
    MockHttpServletRequest request;
    PersonResolution personResolution;

    @InjectMocks
    PersonResolutionController personResolutionController;

    @Mock
    PersonResolutionServiceImpl personResolutionService;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        personResolution = new PersonResolution(0, 0, 0, 0, "www.google.com");
    }

    @Test
    public void testAddPersonResolution() {
        // Expected result
        EntityModel<PersonResolution> entityModel = EntityModel.of(personResolution,
                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId()))
                        .withSelfRel());

        when(personResolutionService.newPersonResolution(any(PersonResolution.class))).thenReturn(entityModel);

        ResponseEntity<?> responseEntity = personResolutionController.newPersonResolution(personResolution);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getLocation().getPath())
                .isEqualTo("/metrics/person-resolution-metric/" + personResolution.getId());
        assertThat(responseEntity.getBody()).isEqualTo("{\"state\": true}");
    }

    @Test
    public void testFailToAddPersonResolution() {
        when(personResolutionService.newPersonResolution(any(PersonResolution.class))).thenReturn(null);

        ResponseEntity<?> responseEntity = personResolutionController.newPersonResolution(null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody()).isEqualTo("{\"state\": false}");
    }

}
