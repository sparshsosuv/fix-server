package com.hes.zf.fix.server.events;

import com.hes.zf.fix.server.service.ExecutionReportService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ReportEventHandler implements ApplicationListener<NewOrderCreatedEvent> {

    @Async
    @EventListener
    public void onApplicationEvent(NewOrderCreatedEvent newOrderCreatedEvent) {
        ExecutionReportService.send(newOrderCreatedEvent.getNewOrderSingle());
    }
}
