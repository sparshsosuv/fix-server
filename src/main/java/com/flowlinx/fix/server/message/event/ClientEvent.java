package com.flowlinx.fix.server.message.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.Message;

@Getter
public class ClientEvent extends ApplicationEvent {

    private Message message;

    public ClientEvent(Message message) {
        super(message);
        this.message = message;
    }

}
