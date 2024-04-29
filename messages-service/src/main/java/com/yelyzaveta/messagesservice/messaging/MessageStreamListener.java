package com.yelyzaveta.messagesservice.messaging;

import com.yelyzaveta.messagesservice.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
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
    public Consumer<Message> messages() {
        return message -> {
            log.info("Got new message {}", message);
            messagesService.saveMessage(message);
        };
    }

}
