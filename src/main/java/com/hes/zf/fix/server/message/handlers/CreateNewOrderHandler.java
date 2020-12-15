package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.infrastructure.OrderRepresentation;
import com.hes.zf.fix.server.message.events.CreateNewOrderEvent;
import com.hes.zf.fix.server.message.events.NewOrderCreatedEvent;
import com.hes.zf.fix.server.service.OrderService;
import com.hes.zf.fix.server.service.SingleOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.FieldNotFound;

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
        OrderRepresentation order = null;
        try {
            order = mapper.messageToOrder(createNewOrderEvent.getMessage());
            service.createOrder(order);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Filed not found", fieldNotFound);
            return;
        }
        publisher.publishEvent(new NewOrderCreatedEvent(this,createNewOrderEvent.getMessage()));
    }
}
