package io.allune.quickfixj.spring.boot.starter.examples.server.service;

import io.allune.quickfixj.spring.boot.starter.examples.server.ServerApplicationAdapter;
import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.FlowlinxApiClient;
import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.OrderRepresentation;
import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

    private FlowlinxApiClient flowlinxApiClient;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(FlowlinxApiClient flowlinxApiClient) {
        this.flowlinxApiClient  = flowlinxApiClient;
    }

    //TODO refactoring
    public void createOrder(OrderRepresentation order) {
        log.info("m=createOrder sending order to api, order{}", order);
        UserRepresentation rep = flowlinxApiClient.auth("globalbank1", "ffrrwl2020");
        flowlinxApiClient.send(order, rep.getToken());

    }

}
