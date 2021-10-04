package com.flowlinx.fix.server.message.event;

import com.flowlinx.fix.server.domain.RoutingTable;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.Message;

@Getter
public class RouteEvent extends ApplicationEvent {

    private Message message;
    private RoutingTable routingTable;

    public RouteEvent(Message message, RoutingTable routingTable) {
        super(message);
        this.message = message;
        this.routingTable = routingTable;
    }



}
