package com.flowlinx.fix.server.message.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.SessionID;
import quickfix.fix44.Message;

@Getter
public class WorkflowEvent extends ApplicationEvent {

    private quickfix.fix44.Message message;
    private SessionID sessionID;

    public WorkflowEvent(Message message, SessionID sessionID) {
        super(message);
        this.message = message;
        this.sessionID = sessionID;
    }



}
