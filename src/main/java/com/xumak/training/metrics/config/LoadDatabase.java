package com.xumak.training.metrics.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xumak.training.metrics.data.BatchLoaderRepository;
import com.xumak.training.metrics.data.PersonResolutionRepository;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.models.PersonResolution;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(PersonResolutionRepository personResolutionRepository,
            BatchLoaderRepository batchLoaderRepository) {

        return args -> {
            personResolutionRepository.save(new PersonResolution(0, 0, 0, 0, "www.google.com"));

            personResolutionRepository.findAll().forEach(personResolution -> log.info("Preloaded " + personResolution));

            batchLoaderRepository.save(new BatchLoader("Filename.txt"));

            batchLoaderRepository.findAll().forEach(batchLoader -> {
                log.info("Preloaded " + batchLoader);
            });

        };
    }
}