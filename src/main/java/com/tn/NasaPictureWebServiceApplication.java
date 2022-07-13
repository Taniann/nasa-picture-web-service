package com.tn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class NasaPictureWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NasaPictureWebServiceApplication.class, args);
    }

    @Scheduled(fixedDelay = 1, timeUnit = DAYS)
    @CacheEvict(value = "picture", allEntries = true)
    public void clearCache() {
        log.warn("Clearing cache...");
    }

}
