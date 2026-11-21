package com.redsalud.andina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RedSaludApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedSaludApplication.class, args);
    }
}
