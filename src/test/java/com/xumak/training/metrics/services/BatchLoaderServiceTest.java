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

import com.xumak.training.metrics.controllers.BatchLoaderController;
import com.xumak.training.metrics.data.BatchLoaderRepository;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.services.impl.BatchLoaderServiceImpl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BatchLoaderServiceTest {

        private String uri = "/metrics/batch-loader-metric";

        // MOCK VARIABLES
        private BatchLoader batchLoader;
        private EntityModel<BatchLoader> entityModel;

        @Autowired
        private MockMvc mockMvc;

        @InjectMocks
        BatchLoaderController controller;

        @Mock
        BatchLoaderServiceImpl service;

        @Mock
        BatchLoaderRepository repo;

        @BeforeEach
        void setup() {
                controller = new BatchLoaderController();
                mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
                MockitoAnnotations.initMocks(this);

                // Mock BatchLoader Object
                batchLoader = new BatchLoader("NewFile.txt");
                // Mock BatchLoaderModelAssembler
                entityModel = EntityModel.of(batchLoader,
                                linkTo(methodOn(BatchLoaderController.class).one(batchLoader.getId()))
                                                .withSelfRel());
        }

        @Test
        void findById() throws Exception {
                doReturn(entityModel).when(service).findById(any(Integer.class));
                this.mockMvc.perform(MockMvcRequestBuilders
                                .get(uri + "/" + batchLoader.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(batchLoader.getId())))
                                .andExpect(jsonPath("$.fileName", is(batchLoader.getFileName())));
                ;
        }

        @Test
        void findBetweenDates() throws Exception {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date start_date = Date.from(ZonedDateTime.now().minusMonths(5).toInstant());
                Date end_date = new Date();
                String string_start_date = dateFormat.format(start_date);
                String string_end_date = dateFormat.format(end_date);

                // Mock second BatchLoader Object and entityModel
                BatchLoader batchLoader2 = new BatchLoader("AnotherFile.txt");
                EntityModel<BatchLoader> entityModel2 = EntityModel.of(batchLoader2,
                                linkTo(methodOn(BatchLoaderController.class).one(batchLoader.getId()))
                                                .withSelfRel());

                // List for the expected result
                List<EntityModel<BatchLoader>> batchLoaders = new ArrayList<>();
                batchLoaders.add(entityModel);
                batchLoaders.add(entityModel2);

                // Expected result
                CollectionModel<EntityModel<BatchLoader>> collectionModel = CollectionModel.of(batchLoaders,
                                linkTo(methodOn(BatchLoaderController.class).all(start_date,
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
