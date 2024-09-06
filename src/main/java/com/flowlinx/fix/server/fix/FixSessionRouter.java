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

//2024-08-21 08:58:37,724 INFO  [QF/J Session dispatcher: FIX.4.2:SOSUV_SERVER_1->SOSUV_VAYU_1] c.f.f.s.fix.ServerApplicationAdapter - fromApp: message.getClass 8=FIX.4.29=17035=D34=33949=SOSUV_VAYU_152=20240821-08:58:37.71756=SOSUV_SERVER_1128=FIXIM129=DTM11=172423071772221=138=88840=244=154=155=QWE59=060=20240821-08:58:37.71710=028 class quickfix.fix42.NewOrderSingle
//<20240821-08:58:37, FIX.4.2:SOSUV_VAYU_1->SOSUV_SERVER_1, outgoing> (8=FIX.4.29=17035=D34=33949=SOSUV_VAYU_152=20240821-08:58:37.71756=SOSUV_SERVER_1128=FIXIM129=DTM11=172423071772221=138=88840=244=154=155=QWE59=060=20240821-08:58:37.71710=028)
//2024-08-21 08:58:37,922 INFO  [QF/J Session dispatcher: FIX.4.2:SOSUV_SERVER_1->SOSUV_VAYU_1] c.f.f.s.fix.ClientApplicationAdapter - toApp: Message=8=FIX.4.29=16735=D34=34049=SOSUV_SERVER52=20240821-08:58:37.92156=FIXIMULATOR128=FIXIM129=DTM11=172423071772221=138=88840=244=154=155=QWE59=060=20240821-08:58:37.71710=007, SessionId=FIX.4.2:SOSUV_SERVER->FIXIMULATOR


//<20240821-09:06:03, FIX.4.2:SOSUV_VAYU_1->SOSUV_SERVER_1, outgoing> (8=FIX.4.29=17035=D34=35449=SOSUV_VAYU_152=20240821-09:06:03.16356=SOSUV_SERVER_1128=FIXIM129=DTM11=172423116316921=138=99940=244=154=155=ABC59=060=20240821-09:06:03.16310=210)
//<20240821-09:06:03, FIX.4.2:FIXIMULATOR->SOSUV_SERVER, incoming> (8=FIX.4.29=16735=D34=35449=SOSUV_SERVER52=20240821-09:06:03.17956=FIXIMULATOR128=FIXIM129=DTM11=172423116316921=138=99940=244=154=155=ABC59=060=20240821-09:06:03.16310=207)
