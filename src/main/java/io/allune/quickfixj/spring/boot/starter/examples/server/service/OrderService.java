package io.allune.quickfixj.spring.boot.starter.examples.server.service;

import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.FlowlinxApiClient;
import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.OrderRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

    @Autowired
    private FlowlinxApiClient flowlinxApiClient;

    @Autowired
    public OrderService(FlowlinxApiClient flowlinxApiClient) {
        this.flowlinxApiClient  = flowlinxApiClient;
    }

    public void createOrder(OrderRepresentation order) {
        flowlinxApiClient.auth("ado", "asd");

    }

}
