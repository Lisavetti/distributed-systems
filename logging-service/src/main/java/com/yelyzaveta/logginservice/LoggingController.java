package com.yelyzaveta.logginservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class LoggingController {

    @Value("${messages.map.name}")
    private String messagesMapKey;
    private final HazelcastInstance hzInstance;
    private IMap<UUID, String> messageMap;
    private final ObjectMapper objectMapper;


    public LoggingController(HazelcastInstance hzInstance,
                             ObjectMapper objectMapper) {
        this.hzInstance = hzInstance;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/save")
    public void saveMessage(@RequestBody String messageBody) {
        try {
            Message message = objectMapper.readValue(messageBody, Message.class);
            messageMap = hzInstance.getMap(messagesMapKey);
            messageMap.put(UUID.fromString(message.uuid), message.message);
            System.out.println("Received message: " + messageBody);
        } catch (Exception e) {
            System.out.println("Unable to deserialize message " + e);
        }
    }

    @GetMapping("/messages")
    public String getAllMessages() {
        List<String> messages = hzInstance.getMap(messagesMapKey)
                .values()
                .stream()
                .map(String::valueOf)
                .toList();
        return String.join("\n", messages);
    }

    static class Message {
        public String uuid;
        public String message;

        @Override
        public String toString() {
            return uuid + " " + message;
        }
    }
}
