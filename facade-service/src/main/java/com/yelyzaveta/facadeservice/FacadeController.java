package com.yelyzaveta.facadeservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
public class FacadeController {

    private final RestTemplate restTemplate;

    public FacadeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/message")
    public String receiveMessage(@RequestBody String message) {
        String uuid = UUID.randomUUID().toString();
        restTemplate.postForObject("http://localhost:8081/save", new Message(uuid, message), Void.class);
        return "Received message with ID: " + uuid;
    }

    @GetMapping("/messages")
    public String getMessages() {
        String loggingServiceMessages = restTemplate.getForObject("http://localhost:8081/messages", String.class);
        String messageServiceResponse = restTemplate.getForObject("http://localhost:8082/static", String.class);
        return loggingServiceMessages + "\n" + messageServiceResponse;
    }

    static class Message {
        public String messageId;
        public String message;

        public Message(String messageId, String message) {
            this.messageId = messageId;
            this.message = message;
        }
    }

}
