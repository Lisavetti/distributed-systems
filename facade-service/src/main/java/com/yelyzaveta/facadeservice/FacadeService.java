package com.yelyzaveta.facadeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelyzaveta.facadeservice.api.ApiServiceConfig;
import com.yelyzaveta.facadeservice.messaging.MessageStreamPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class FacadeService {

    private static final Logger log = LoggerFactory.getLogger(FacadeService.class);
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final MessageStreamPublisher publisher;
    private final ApiServiceConfig apiServiceConfig;
    private final LoadBalancerClient loadBalancerClient;
    private final ObjectMapper objectMapper;

    public FacadeService(MessageStreamPublisher publisher,
                         ApiServiceConfig apiServiceConfig,
                         LoadBalancerClient loadBalancerClient,
                         ObjectMapper objectMapper) {
        this.publisher = publisher;
        this.apiServiceConfig = apiServiceConfig;
        this.loadBalancerClient = loadBalancerClient;
        this.objectMapper = objectMapper;
    }

    public String sendMessage(String messageBody) {
        return executeHttpOperation(() -> {
            try {
                String uuid = UUID.randomUUID().toString();
                Message parsedMessage = objectMapper.readValue(messageBody, Message.class);
                Message message = new Message(uuid, parsedMessage.message());
                ServiceInstance serviceInstance = getServiceInstance(apiServiceConfig.getLoggingServiceName());
                String url = buildUrl("save", serviceInstance.getHost(), serviceInstance.getPort());
                HttpResponse<String> response = sendHttpRequest(HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(message)))
                        .build());
                publisher.publishMessage(message);
                return response.body();
            } catch (JsonProcessingException e) {
                log.error("JSON processing error while sending message", e);
                throw new RuntimeException("Error processing JSON during send operation", e);
            }
        });
    }

    public String getMessageLogs() {
        return executeHttpOperation(() -> {
            ServiceInstance serviceInstance = getServiceInstance(apiServiceConfig.getLoggingServiceName());
            String url = buildUrl("messages", serviceInstance.getHost(), serviceInstance.getPort());
            HttpResponse<String> response = sendHttpRequest(HttpRequest.newBuilder().uri(URI.create(url)).GET().build());
            return response.body();
        });
    }

    public String getMessages() {
        return executeHttpOperation(() -> {
            ServiceInstance serviceInstance = getServiceInstance(apiServiceConfig.getMessagesServiceName());
            String url = buildUrl("static", serviceInstance.getHost(), serviceInstance.getPort());
            HttpResponse<String> response = sendHttpRequest(HttpRequest.newBuilder().uri(URI.create(url)).GET().build());
            return response.body();
        });
    }

    private String buildUrl(String path, String host, int port) {
        return String.format("http://%s:%d/%s", host, port, path);
    }

    private ServiceInstance getServiceInstance(String serviceName) {
        return Optional.ofNullable(loadBalancerClient.choose(serviceName))
                .orElseThrow(() -> new IllegalArgumentException(serviceName + " service was not found"));
    }

    private HttpResponse<String> sendHttpRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("HTTP operation failed", e);
            throw new RuntimeException("HTTP operation failed: " + e.getMessage(), e);
        }
    }

    private String executeHttpOperation(Supplier<String> operation) {
        try {
            return operation.get();
        } catch (RuntimeException e) {
            log.error("HTTP operation failed", e);
            throw new RuntimeException("HTTP operation failed: " + e.getMessage(), e);
        }
    }
}
