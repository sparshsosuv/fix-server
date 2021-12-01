package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.message.event.RouteEvent;
import com.flowlinx.fix.server.type.FixTargetSession;
import com.flowlinx.fix.server.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.Message;
import quickfix.Session;
import quickfix.field.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
public class RouteEventHandler implements ApplicationListener<RouteEvent> {

    @Override
    public void onApplicationEvent(RouteEvent event) {

        try{
            final Message message = event.getMessage();
            final RoutingTable route = event.getRoutingTable();

            final String senderCompID =  AppUtils.getString( message.getHeader(), SenderCompID.FIELD );
            message.getHeader().setField( new OnBehalfOfCompID( senderCompID ) );

            log.info("Routing message FROM: {}, TO: {}", route.getSenderCompID(), route.getTargetCompID()  );
            log.info("MESSAGE: {}",  message.toString() );

            final boolean result = Session.sendToTarget( message, route.getSenderCompID(), route.getTargetCompID() );

            if(!result){

                message.getHeader().removeField( OnBehalfOfCompID.FIELD );
                message.setField( new ExecType( ExecType.REJECTED ) );
                message.setField( new OrdStatus( OrdStatus.REJECTED ));
                message.setField( new TransactTime( LocalDateTime.now() ) );
                message.setField( new Text( StringUtils.replace( route.getTargetCompID() + " is offline", " ", "_" )  ) );

                final String server = FixTargetSession.from( senderCompID ).get().getSender().name();

                Session.sendToTarget( message, server, senderCompID );
            }

        } catch (Exception ex) {
            ex.printStackTrace();;
        }

    }
}
