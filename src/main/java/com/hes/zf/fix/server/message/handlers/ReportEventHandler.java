package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.message.events.NewOrderCreatedEvent;
import com.hes.zf.fix.server.service.ExecutionReportService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import quickfix.field.OrdStatus;

@Component
public class ReportEventHandler implements ApplicationListener<NewOrderCreatedEvent> {

    @EventListener
    public void onApplicationEvent(NewOrderCreatedEvent newOrderCreatedEvent) {

        ExecutionReportService.send(newOrderCreatedEvent.getNewOrderSingle(), new OrdStatus(OrdStatus.REJECTED));
    }
}
