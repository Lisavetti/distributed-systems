package com.yelyzaveta.messagesservice;

import com.yelyzaveta.messagesservice.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class MessagesService {

    private final Set<Message> messagesMap = new ConcurrentSkipListSet<>();

    public void saveMessage(Message message) {
        messagesMap.add(message);
    }

    public String getAllMessages() {
        return String.join("\n", messagesMap
                .stream()
                .map(Message::message)
                .map(String::valueOf)
                .toList());
    }
}
