package com.yelyzaveta.messagesservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesController {

    @GetMapping("/static")
    public String getStaticMessage() {
        return "Messages service not implemented yet";
    }
}
