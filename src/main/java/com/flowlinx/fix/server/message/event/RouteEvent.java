package com.flowlinx.fix.server.message.event;

import com.flowlinx.fix.server.domain.RoutingTable;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.Message;
import quickfix.SessionID;

@Getter
public class RouteEvent extends ApplicationEvent {

    private Message message;
    private RoutingTable routingTable;
    private SessionID sessionId;

    public RouteEvent(Message message, RoutingTable routingTable, SessionID sessionId) {
        super(message);
        this.message = message;
        this.routingTable = routingTable;
        this.sessionId = sessionId;
    }



}
