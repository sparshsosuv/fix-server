package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.message.events.OrderCancelRequestEvent;
import com.hes.zf.fix.server.type.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;

@Component
public class OrderCancelRequestHandler implements ApplicationListener<OrderCancelRequestEvent> {

    private static final Logger log = LoggerFactory.getLogger(OrderCancelReplaceHandler.class);

    @Override
    public void onApplicationEvent(OrderCancelRequestEvent orderCancelRequestEvent) {
        log.info("SENDING OrderCancelRequestEvent FROM SERVER TO WORKFLOW");
        try {
            Session.sendToTarget( orderCancelRequestEvent.getMessage(), FixSession.SERVER.name(), FixSession.WORKFLOW.name() );
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            log.error("Session not found");
        }
    }
}
