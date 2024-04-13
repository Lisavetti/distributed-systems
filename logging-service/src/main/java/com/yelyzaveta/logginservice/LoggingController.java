package com.yelyzaveta.logginservice;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class LoggingController {

    private final HazelcastInstance hzInstance;


    public LoggingController(HazelcastInstance hzInstance) {
        this.hzInstance = hzInstance;
    }

    @PostMapping("/save")
    public void saveMessage(@RequestBody Message message) {
        IMap<UUID, String> messageMap = hzInstance.getMap("lab-messages");
        messageMap.put(UUID.fromString(message.uuid), message.message);
        System.out.println("Received message: " + message);
    }

    @GetMapping("/messages")
    public String getAllMessages() {
        List<String> messages = hzInstance.getMap("lab-messages")
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
