package com.podmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PodMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(PodMateApplication.class, args);
    }

}

