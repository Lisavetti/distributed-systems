package com.yelyzaveta.facadeservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "logging-service")
public class LoggingServiceConfig {
    private List<Integer> targetPorts;

    public List<Integer> getTargetPorts() {
        return targetPorts;
    }

    public void setTargetPorts(List<Integer> targetPorts) {
        this.targetPorts = targetPorts;
    }
}
