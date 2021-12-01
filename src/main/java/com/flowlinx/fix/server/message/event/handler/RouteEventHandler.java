package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.message.event.RouteEvent;
import com.flowlinx.fix.server.type.FixTargetSession;
import com.flowlinx.fix.server.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Mapper mapper;

    @Override
    public void onApplicationEvent(RouteEvent event) {

        try{
            final Message message = mapper.map( event.getMessage(), Message.class ) ;
            final RoutingTable route = event.getRoutingTable();


            final String targetCompID =  AppUtils.getString( message.getHeader(), TargetCompID.FIELD );
            final String senderCompID =  AppUtils.getString( message.getHeader(), SenderCompID.FIELD );
            message.getHeader().setField( new OnBehalfOfCompID( senderCompID ) );

            log.info("Routing message FROM: {}, TO: {}", route.getSenderCompID(), route.getTargetCompID()  );
            log.info("ROUTE: ", route.toString()  );
            log.info("MESSAGE: {}",  message.toString() );

            final boolean result = Session.sendToTarget( message, route.getSenderCompID(), route.getTargetCompID() );

            if(!result){

                final Message reject = mapper.map( event.getMessage(), Message.class ) ;
                reject.setField( new ExecType( ExecType.REJECTED ) );
                reject.setField( new OrdStatus( OrdStatus.REJECTED ));
                reject.setField( new TransactTime( LocalDateTime.now() ) );
                reject.setField( new Text( StringUtils.replace( route.getTargetCompID() + " is currently offline", " ", "_" )  ) );

                Session.sendToTarget( reject, targetCompID, senderCompID );
            }

        } catch (Exception ex) {
            ex.printStackTrace();;
        }

    }
}
