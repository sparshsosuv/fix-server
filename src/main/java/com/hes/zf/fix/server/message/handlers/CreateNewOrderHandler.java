package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.message.events.CreateNewOrderEvent;
import com.hes.zf.fix.server.service.OrderService;
import com.hes.zf.fix.server.service.SingleOrderMapper;
import com.hes.zf.fix.server.type.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;

@Component
public class CreateNewOrderHandler implements ApplicationListener<CreateNewOrderEvent> {

    private static final Logger log = LoggerFactory.getLogger(CreateNewOrderHandler.class);

    private SingleOrderMapper mapper;
    private OrderService service;
    private ApplicationEventPublisher publisher;


    public CreateNewOrderHandler(SingleOrderMapper mapper, OrderService service, ApplicationEventPublisher publisher) {
        this.mapper = mapper;
        this.service = service;
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(CreateNewOrderEvent createNewOrderEvent) {
        log.info("SENDING createNewOrderEvent FROM SERVER TO WORKFLOW");
        try {
            Session.sendToTarget( createNewOrderEvent.getMessage(), FixSession.SERVER.name(), FixSession.WORKFLOW.name() );
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            log.error("Session not found");
        }
    }
}
