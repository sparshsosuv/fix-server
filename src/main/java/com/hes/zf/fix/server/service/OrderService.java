package com.hes.zf.fix.server.service;

import com.hes.zf.fix.server.infrastructure.FlowlinxApiClient;
import com.hes.zf.fix.server.infrastructure.OrderRepresentation;
import com.hes.zf.fix.server.infrastructure.UserRepresentation;
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

    public void createOrder(OrderRepresentation order) {
        log.info("m=createOrder sending order to api, order{}", order);
        UserRepresentation rep = flowlinxApiClient.auth("globalbank1", "ffrrwl2020");
        flowlinxApiClient.send(order, rep.getToken());

    }

}
