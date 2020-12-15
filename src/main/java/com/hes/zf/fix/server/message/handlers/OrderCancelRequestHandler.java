package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.message.events.OrderCancelRequestEvent;
import org.springframework.context.ApplicationListener;

public class OrderCancelRequestHandler implements ApplicationListener<OrderCancelRequestEvent> {

    @Override
    public void onApplicationEvent(OrderCancelRequestEvent orderCancelRequestEvent) {
        System.out.println(orderCancelRequestEvent.getMessage());
    }
}
