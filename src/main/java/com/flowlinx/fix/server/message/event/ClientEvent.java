package com.flowlinx.fix.server.message.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.SessionID;

@Getter
public class ClientEvent extends ApplicationEvent {

    private quickfix.fix44.Message message;
    private SessionID sessionID;

    public ClientEvent(quickfix.fix44.Message message, SessionID sessionID) {
        super(message);
        this.message = message;
        this.sessionID = sessionID;
    }

}
