package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.message.event.ClientEvent;
import com.flowlinx.fix.server.type.FixTargetSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.DeliverToCompID;
import quickfix.fix44.Message;

import java.util.Optional;

@Slf4j
@Component
public class ClientRoutingHandler implements ApplicationListener<ClientEvent> {

    @Override
    public void onApplicationEvent(ClientEvent event) {

        final Message message = event.getMessage();

        try {
            final String deliverToCompID = message.getHeader().getString( DeliverToCompID.FIELD );

            final Optional<FixTargetSession> opt = FixTargetSession.from( deliverToCompID );

            if( opt.isPresent() ) {
                Session.sendToTarget( message, opt.get().getSender().name(), deliverToCompID );
            }

        } catch (FieldNotFound | SessionNotFound e) {
            e.printStackTrace();
        }

    }
}
