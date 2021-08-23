package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.utils.FixConstants;
import com.flowlinx.fix.server.message.event.ClientEvent;
import com.flowlinx.fix.server.type.FixTargetSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;

import java.util.Optional;

@Slf4j
@Component
public class ClientRoutingHandler implements ApplicationListener<ClientEvent> {

    @Override
    public void onApplicationEvent(ClientEvent event) {

        final Message message = event.getMessage();

        try {
            final String flxTargetCompId = message.getString(FixConstants.FLX_TARGET_COMP_ID );
            message.removeField( FixConstants.FLX_TARGET_COMP_ID );
            final Optional<FixTargetSession> opt = FixTargetSession.from( flxTargetCompId );

            if( opt.isPresent() ) {
                Session.sendToTarget( message, opt.get().getSender().name(), flxTargetCompId );
            }

        } catch (FieldNotFound | SessionNotFound e) {
            e.printStackTrace();
        }

    }
}
