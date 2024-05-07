package com.yelyzaveta.facadeservice.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.services")
public class ApiServiceConfig {
    private String loggingServiceName;
    private String messagesServiceName;

    public String getLoggingServiceName() {
        return loggingServiceName;
    }

    public void setLoggingServiceName(String loggingServiceName) {
        this.loggingServiceName = loggingServiceName;
    }

    public String getMessagesServiceName() {
        return messagesServiceName;
    }

    public void setMessagesServiceName(String messagesServiceName) {
        this.messagesServiceName = messagesServiceName;
    }
}
