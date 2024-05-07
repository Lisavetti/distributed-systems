package com.yelyzaveta.facadeservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FacadeController {

    private final FacadeService facadeService;

    public FacadeController(FacadeService facadeService) {
        this.facadeService = facadeService;
    }

    @PostMapping("/message")
    public String receiveMessage(@RequestBody String messageBody) {
        String messageId = facadeService.sendMessage(messageBody);

        return "Received message with ID: " + messageId;
    }

    @GetMapping("/messageLogs")
    public String getMessageLogs() {
        return facadeService.getMessageLogs();
    }

    @GetMapping("/messages")
    public String getMessages() {
        return facadeService.getMessages();
    }

}
