package com.yelyzaveta.facadeservice.messaging;

import com.yelyzaveta.facadeservice.FacadeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

@Component
public class MessageStreamPublisher {

    private final Logger log = LoggerFactory.getLogger(MessageStreamPublisher.class);
    private final StreamBridge streamBridge;
    private final MimeType protobufMimeType;

    public MessageStreamPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.protobufMimeType = new MimeType("text", "plain");
    }

    public void publishMessage(FacadeController.Message message) {
        try {
            send(message);
        } catch (Exception e) {
            log.error("Can not publish message {}", message, e);
        }
    }

    private void send(FacadeController.Message message) {
        streamBridge.send("messages-out-0", MessageBuilder.withPayload(message)
                .setHeader("partitionKey", message.hashCode() % 10)
                .build(), protobufMimeType);
    }
}
