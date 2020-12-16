package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.message.events.OrderCancelReplaceEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelReplaceHandler implements ApplicationListener<OrderCancelReplaceEvent> {

    @Override
    public void onApplicationEvent(OrderCancelReplaceEvent orderCancelReplaceEvent) {
        System.out.println(orderCancelReplaceEvent.getMessage());
    }
}
