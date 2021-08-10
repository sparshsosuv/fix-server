package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.message.event.WorkflowEvent;
import com.flowlinx.fix.server.type.FixSenderSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.OnBehalfOfCompID;
import quickfix.field.SenderCompID;
import quickfix.fix44.Message;

@Slf4j
@Component
public class WorkflowRoutingHandler implements ApplicationListener<WorkflowEvent> {

    private static final String FLOWLINX_WORKFLOW = "FLOWLINX_WORKFLOW";

    @Override
    public void onApplicationEvent(WorkflowEvent event) {

        try{
            final Message message = event.getMessage();
            final String onBehalfOfCompId = message.getHeader().getString( SenderCompID.FIELD );

            message.getHeader().setField( new OnBehalfOfCompID( onBehalfOfCompId ) );
            Session.sendToTarget( message, FixSenderSession.FLOWLINX_SERVER.name(), FLOWLINX_WORKFLOW );

        } catch (SessionNotFound | FieldNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            log.error("Session not found");
        }
    }

}
