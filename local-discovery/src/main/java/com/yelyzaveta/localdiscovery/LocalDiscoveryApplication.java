package com.yelyzaveta.localdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LocalDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalDiscoveryApplication.class, args);
    }

}
