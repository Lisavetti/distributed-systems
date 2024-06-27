package com.yelyzaveta.facadeservice.api.messagingservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "messaging-service")
public class MessagingServiceConfig {
    private List<Integer> targetPorts;

    public List<Integer> getTargetPorts() {
        return targetPorts;
    }

    public void setTargetPorts(List<Integer> targetPorts) {
        this.targetPorts = targetPorts;
    }
}
