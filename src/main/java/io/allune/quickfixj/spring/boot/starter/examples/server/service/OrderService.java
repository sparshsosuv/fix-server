package io.allune.quickfixj.spring.boot.starter.examples.server.service;

import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.FlowlinxApiClient;
import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.OrderRepresentation;
import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

    private FlowlinxApiClient flowlinxApiClient;

    @Autowired
    public OrderService(FlowlinxApiClient flowlinxApiClient) {
        this.flowlinxApiClient  = flowlinxApiClient;
    }

    //TODO refactoring
    public void createOrder(OrderRepresentation order) {
        UserRepresentation rep = flowlinxApiClient.auth("ado", "asd");
        flowlinxApiClient.send(order, rep.getToken());

    }

}
