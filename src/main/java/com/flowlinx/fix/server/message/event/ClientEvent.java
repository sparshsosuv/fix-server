package com.flowlinx.fix.server.message.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.Message;
import quickfix.SessionID;

@Getter
public class ClientEvent extends ApplicationEvent {

    private Message message;
    private SessionID sessionId;

    public ClientEvent(Message message, SessionID sessionId) {
        super(message);
        this.message = message;
        this.sessionId = sessionId;
    }

}
