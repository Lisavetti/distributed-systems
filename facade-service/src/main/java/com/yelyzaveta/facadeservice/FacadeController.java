package com.yelyzaveta.facadeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelyzaveta.facadeservice.api.loggingservice.LoggingServiceConfig;
import com.yelyzaveta.facadeservice.api.messagingservice.MessagingServiceConfig;
import com.yelyzaveta.facadeservice.messaging.MessageStreamPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
public class FacadeController {

    private final ObjectMapper objectMapper;
    private final MessageStreamPublisher messageStreamPublisher;
    private final LoggingServiceConfig loggingServiceConfig;
    private final MessagingServiceConfig messagingServiceConfig;
    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public FacadeController(ObjectMapper objectMapper,
                            MessageStreamPublisher messageStreamPublisher,
                            LoggingServiceConfig loggingServiceConfig,
                            MessagingServiceConfig messagingServiceConfig,
                            RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.messageStreamPublisher = messageStreamPublisher;
        this.loggingServiceConfig = loggingServiceConfig;
        this.messagingServiceConfig = messagingServiceConfig;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/message")
    public String receiveMessage(@RequestBody String messageBody) {
        List<Integer> ports = loggingServiceConfig.getTargetPorts();
        int randomPort = ports.get(random.nextInt(ports.size()));

        String uuid = UUID.randomUUID().toString();
        Message message = new Message(uuid, messageBody);
        restTemplate.postForObject("http://localhost:" + randomPort + "/save", message, Void.class);
        messageStreamPublisher.publishMessage(message);
        return "Received message with ID: " + uuid;
    }

    @GetMapping("/messages")
    public String getMessages() {
        List<Integer> loggingServicePorts = loggingServiceConfig.getTargetPorts();
        StringBuilder responses = new StringBuilder();

        for (int port : loggingServicePorts) {
            try {
                String response = restTemplate.getForObject("http://localhost:" + port + "/messages", String.class);
                responses.append(response).append("\n");
                break;
            } catch (RestClientException e) {
                continue; // Try the next port
            }
        }
        List<Integer> messagingServiceConfigTargetPorts = messagingServiceConfig.getTargetPorts();
        responses.append("--------");
        for (int port : messagingServiceConfigTargetPorts) {
            try {
                String messageServiceResponse = restTemplate.getForObject("http://localhost:" + port + "/static", String.class);
                responses.append(messageServiceResponse).append("\n");
                break;
            } catch (RestClientException e) {
                continue; // Try the next port
            }
        }

        return responses.toString().trim();
    }

    public static class Message {
        public String uuid;
        public String message;

        public Message(String uuid, String message) {
            this.uuid = uuid;
            this.message = message;
        }
    }

}
