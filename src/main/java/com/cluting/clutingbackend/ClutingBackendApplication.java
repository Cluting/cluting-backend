package com.cluting.clutingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan(basePackages = "com.cluting.clutingbackend")
@EnableScheduling
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ClutingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClutingBackendApplication.class, args);
    }

}
