package com.flowlinx.fix.server.message.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.SessionID;
import quickfix.fix44.Message;

@Getter
public class WorkflowEvent extends ApplicationEvent {

    private Message message;

    public WorkflowEvent(Message message) {
        super(message);
        this.message = message;
    }



}
