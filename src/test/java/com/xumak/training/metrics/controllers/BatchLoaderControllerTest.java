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

import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.services.impl.BatchLoaderServiceImpl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class BatchLoaderControllerTest {

    // MOCK REQUEST
    MockHttpServletRequest request;

    @InjectMocks
    BatchLoaderController batchLoaderController;

    @Mock
    BatchLoaderServiceImpl batchLoaderService;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testAddBatchLoader() {
        BatchLoader batchLoader = new BatchLoader("NewFile.txt");

        // Expected result
        EntityModel<BatchLoader> entityModel = EntityModel.of(batchLoader,
                linkTo(methodOn(BatchLoaderController.class).one(batchLoader.getId()))
                        .withSelfRel());

        when(batchLoaderService.newBatchLoader(any(BatchLoader.class))).thenReturn(entityModel);

        ResponseEntity<?> responseEntity = batchLoaderController.newBatchLoader(batchLoader);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getLocation().getPath())
                .isEqualTo("/metrics/batch-loader-metric/" + batchLoader.getId());
        assertThat(responseEntity.getBody()).isEqualTo("{\"state\": true}");
    }

    @Test
    public void testFailToAddBatchLoader() {
        when(batchLoaderService.newBatchLoader(any(BatchLoader.class))).thenReturn(null);

        ResponseEntity<?> responseEntity = batchLoaderController.newBatchLoader(null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody()).isEqualTo("{\"state\": false}");
    }

}
