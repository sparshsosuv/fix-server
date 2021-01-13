package com.flowlinx.fix.server.message.events;

import org.springframework.context.ApplicationEvent;

public class OrderCancelReplaceEvent extends ApplicationEvent {

    public OrderCancelReplaceEvent(Object source, quickfix.fix44.OrderCancelReplaceRequest message) {
        super(source);
        this.message = message;

    }

    private quickfix.fix44.OrderCancelReplaceRequest message;

    public quickfix.fix44.OrderCancelReplaceRequest getMessage() {
        return message;
    }

}
