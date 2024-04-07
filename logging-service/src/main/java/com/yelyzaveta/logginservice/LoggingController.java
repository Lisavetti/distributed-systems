package com.yelyzaveta.logginservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class LoggingController {

    private final Map<String, String> messages = new HashMap<>();

    @PostMapping("/save")
    public void saveMessage(@RequestBody Message message) {
        messages.put(message.uuid, message.message);
        System.out.println("Received and saved message: " + message.message);
    }

    @GetMapping("/messages")
    public String getAllMessages() {
        return String.join("\n", messages.values());
    }

    static class Message {
        public String uuid;
        public String message;
    }
}
