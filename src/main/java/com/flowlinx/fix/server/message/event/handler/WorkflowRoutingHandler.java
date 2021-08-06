package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.message.event.WorkflowEvent;
import com.flowlinx.fix.server.type.FixSenderSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.OnBehalfOfCompID;
import quickfix.fix44.Message;

@Slf4j
@Component
public class WorkflowRoutingHandler implements ApplicationListener<WorkflowEvent> {

    private static final String WORKFLOW = "WORKFLOW";

    @Override
    public void onApplicationEvent(WorkflowEvent event) {

        try{
            final Message message = event.getMessage();
            message.getHeader().setField( new OnBehalfOfCompID( event.getSessionID().getTargetCompID() ) );
            Session.sendToTarget( message, FixSenderSession.SERVER.name(), WORKFLOW );

        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            log.error("Session not found");
        }
    }

}
