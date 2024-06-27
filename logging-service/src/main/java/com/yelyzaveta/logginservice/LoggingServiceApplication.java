package com.yelyzaveta.logginservice;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LoggingServiceApplication {

    @Bean
    public HazelcastInstance hzInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    public static void main(String[] args) {
        SpringApplication.run(LoggingServiceApplication.class, args);
    }

}