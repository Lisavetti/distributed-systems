package com.yelyzaveta.facadeservice;

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

    private final LoggingServiceConfig loggingServiceConfig;
    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public FacadeController(LoggingServiceConfig loggingServiceConfig, RestTemplate restTemplate) {
        this.loggingServiceConfig = loggingServiceConfig;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/message")
    public String receiveMessage(@RequestBody String message) {
        List<Integer> ports = loggingServiceConfig.getTargetPorts();
        int randomPort = ports.get(random.nextInt(ports.size()));

        String uuid = UUID.randomUUID().toString();
        restTemplate.postForObject("http://localhost:" + randomPort + "/save", new Message(uuid, message), Void.class);
        return "Received message with ID: " + uuid;
    }

    @GetMapping("/messages")
    public String getMessages() {
        List<Integer> ports = loggingServiceConfig.getTargetPorts();
        StringBuilder responses = new StringBuilder();

        String messageServiceResponse = restTemplate.getForObject("http://localhost:8082/static", String.class);
        for (int port : ports) {
            try {
                String response = restTemplate.getForObject("http://localhost:" + port + "/messages", String.class);
                responses.append(response).append("\n");
                break;
            } catch (RestClientException e) {
                continue; // Try the next port
            }
        }
        responses.append(messageServiceResponse);

        return responses.toString().trim();
    }

    static class Message {
        public String uuid;
        public String message;

        public Message(String uuid, String message) {
            this.uuid = uuid;
            this.message = message;
        }
    }

}
