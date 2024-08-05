package com.flowlinx.fix.server.fix;

import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.message.event.ClientEvent;
import com.flowlinx.fix.server.message.event.RouteEvent;
import com.flowlinx.fix.server.message.event.WorkflowEvent;
import com.flowlinx.fix.server.service.RoutingTableService;
import com.flowlinx.fix.server.service.RuleService;
import com.flowlinx.fix.server.type.FixTargetSession;
import com.flowlinx.fix.server.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.DeliverToCompID;
import quickfix.field.TargetCompID;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class FixSessionRouter {

    @Autowired
    private RoutingTableService routingTableService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private Mapper mapper;

    public void route(Message message, SessionID sessionID) {

        final String deliverToCompId = AppUtils.getString( message.getHeader(), DeliverToCompID.FIELD );

        final Optional<RoutingTable> opt = routingTableService.findByDeliverToCompID( deliverToCompId );

        log.info("------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.." );

        if( opt.isPresent() ) {

            final RoutingTable route =  mapper.map( opt.get(), RoutingTable.class );

            log.info("Routing message to r{}", route );

            publisher.publishEvent( new RouteEvent( message, route, sessionID ) );

        } else {

            final Optional<FixTargetSession> optSession = Arrays.stream( FixTargetSession.values() )
                    .filter( t -> t.name().equalsIgnoreCase( sessionID.getTargetCompID() ) )
                    .findFirst();

            log.info("FixSessionRouter - Not Routing message={}, WorkflowEvent={}", message, optSession.isPresent() );

            if( optSession.isPresent() ){
                publisher.publishEvent( new WorkflowEvent( message ) );

            } else {
                publisher.publishEvent( new ClientEvent( message, sessionID ) );
            }

        }
    }

}