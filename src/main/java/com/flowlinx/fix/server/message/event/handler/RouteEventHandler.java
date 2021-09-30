package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.message.event.RouteEvent;
import com.flowlinx.fix.server.type.FixTargetSession;
import com.flowlinx.fix.server.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.Message;
import quickfix.Session;
import quickfix.field.SenderCompID;

import java.util.Optional;

@Slf4j
@Component
public class RouteEventHandler implements ApplicationListener<RouteEvent> {

    @Override
    public void onApplicationEvent(RouteEvent event) {

        try{
            final Message message = event.getMessage();
            final RoutingTable route = event.getRoutingTable();
            Session.sendToTarget( message, route.getSenderCompID(), route.getTargetCompID() );

        } catch (Exception ex) {
            ex.printStackTrace();;
        }

    }
}
