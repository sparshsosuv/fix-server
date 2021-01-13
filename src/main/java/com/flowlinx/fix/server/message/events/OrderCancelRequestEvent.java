package com.flowlinx.fix.server.message.events;

import org.springframework.context.ApplicationEvent;

public class OrderCancelRequestEvent  extends ApplicationEvent {

    public OrderCancelRequestEvent(Object source, quickfix.fix44.OrderCancelRequest message) {
        super(source);
        this.message = message;
    }

    private quickfix.fix44.OrderCancelRequest message;

    public quickfix.fix44.OrderCancelRequest getMessage() {
        return message;
    }
}