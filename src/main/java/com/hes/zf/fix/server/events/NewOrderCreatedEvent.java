package com.hes.zf.fix.server.events;

import org.springframework.context.ApplicationEvent;
import quickfix.fix44.NewOrderSingle;

public class NewOrderCreatedEvent extends ApplicationEvent {

    public NewOrderCreatedEvent(Object source, quickfix.fix44.NewOrderSingle newOrderSingle) {
        super(source);
        this.newOrderSingle = newOrderSingle;

    }

    private quickfix.fix44.NewOrderSingle newOrderSingle;

    public NewOrderSingle getNewOrderSingle() {
        return newOrderSingle;
    }
}
