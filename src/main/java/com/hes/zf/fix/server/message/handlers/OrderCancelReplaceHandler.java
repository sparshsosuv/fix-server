package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.message.events.OrderCancelReplaceEvent;
import com.hes.zf.fix.server.service.ExecutionReportService;
import com.hes.zf.fix.server.type.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.OrdStatus;

@Component
public class OrderCancelReplaceHandler implements ApplicationListener<OrderCancelReplaceEvent> {

    private static final Logger log = LoggerFactory.getLogger(OrderCancelReplaceHandler.class);

    @Override
    public void onApplicationEvent(OrderCancelReplaceEvent orderCancelReplaceEvent) {

        log.info("SENDING OrderCancelReplaceEvent FROM SERVER TO DEALING_SHEET");
        try {
            Session.sendToTarget( orderCancelReplaceEvent.getMessage(), FixSession.SERVER.name(), FixSession.WORKFLOW.name() );
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            log.error("Session not found");
        }
    }
}
