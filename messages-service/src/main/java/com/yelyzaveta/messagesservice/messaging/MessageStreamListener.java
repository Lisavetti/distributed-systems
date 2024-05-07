package com.yelyzaveta.messagesservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelyzaveta.messagesservice.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MessageStreamListener {

    private final Logger log = LoggerFactory.getLogger(MessageStreamListener.class);
    private final MessagesService messagesService;

    public MessageStreamListener(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Bean
    public Consumer<org.springframework.messaging.Message<String>> messages() {
        return message -> {
            String payload = message.getPayload();
            try {
                log.info("Got new message {}", payload);
                messagesService.saveMessage(deserialize(payload));
            } catch (Exception e) {
                log.error("Unable to process message {}", payload, e);
            }
        };
    }

    private com.yelyzaveta.messagesservice.messaging.Message deserialize(String json) throws JsonProcessingException {
        // Implement JSON deserialization to Message object
        // This might be using Jackson, Gson, etc.
        return new ObjectMapper().readValue(json, com.yelyzaveta.messagesservice.messaging.Message.class);
    }

}
