package com.hes.zf.fix.server.events;

import org.springframework.context.ApplicationEvent;
import quickfix.fix44.NewOrderSingle;

public class CreateNewOrderEvent extends ApplicationEvent {

    public CreateNewOrderEvent(Object source, quickfix.fix44.NewOrderSingle message) {
        super(source);
        this.message = message;
    }

    private quickfix.fix44.NewOrderSingle message;

    public NewOrderSingle getMessage() {
        return message;
    }
}
