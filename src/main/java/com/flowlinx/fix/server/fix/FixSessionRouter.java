package com.flowlinx.fix.server.fix;

import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.message.event.ClientEvent;
import com.flowlinx.fix.server.message.event.RouteEvent;
import com.flowlinx.fix.server.message.event.WorkflowEvent;
import com.flowlinx.fix.server.service.RoutingTableService;
import com.flowlinx.fix.server.type.FixTargetSession;
import com.flowlinx.fix.server.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.DeliverToCompID;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class FixSessionRouter {

    @Autowired
    private RoutingTableService routingTableService;

    @Autowired
    private ApplicationEventPublisher publisher;

    public void route(Message message, SessionID sessionID) {

        final String deliverToCompId = AppUtils.getString( message.getHeader(), DeliverToCompID.FIELD );

        final Optional<RoutingTable> opt = routingTableService.findByDeliverToCompID( deliverToCompId );

        if( opt.isPresent() ) {
            publisher.publishEvent( new RouteEvent( message, opt.get() ) );

        } else {

            final Optional<FixTargetSession> optSession = Arrays.stream( FixTargetSession.values() )
                    .filter( t -> t.name().equalsIgnoreCase( sessionID.getTargetCompID() ) )
                    .findFirst();

            if( optSession.isPresent() ){
                publisher.publishEvent( new WorkflowEvent( message ) );

            } else {
                publisher.publishEvent( new ClientEvent( message ) );
            }

        }
    }

}