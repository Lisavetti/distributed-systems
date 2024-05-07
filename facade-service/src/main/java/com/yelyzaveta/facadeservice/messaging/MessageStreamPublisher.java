package com.yelyzaveta.facadeservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelyzaveta.facadeservice.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Component
public class MessageStreamPublisher {

    private final Logger log = LoggerFactory.getLogger(MessageStreamPublisher.class);
    private final StreamBridge streamBridge;

    public MessageStreamPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishMessage(Message message) {
        try {
            send(message);
        } catch (Exception e) {
            log.error("Can not publish message {}", message, e);
        }
    }

    private void send(Message message) throws JsonProcessingException {
        streamBridge.send("messages-out-0", MessageBuilder.withPayload(convertToJson(message))
                .setHeader("partitionKey", message.hashCode() % 10)
                .setHeader("contentType", MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build());
    }

    private String convertToJson(Message message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(message);
    }
}
