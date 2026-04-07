package com.trending.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrendingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrendingApiApplication.class, args);
    }
}
