package com.yelyzaveta.messagesservice;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class MessagesService {

    private final Set<String> messagesMap = new ConcurrentSkipListSet<>();

    public void saveMessage(String message) {
        messagesMap.add(message);
    }

    public String getAllMessages() {
        return String.join("\n", messagesMap
                .stream()
                .map(String::valueOf)
                .toList());
    }
}
