package com.haiboyan.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class OrganizationApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(OrganizationApplication.class, args);
    }
}
