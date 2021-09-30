package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.utils.FixConstants;
import com.flowlinx.fix.server.message.event.WorkflowEvent;
import com.flowlinx.fix.server.type.FixSenderSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.SenderCompID;
import quickfix.fix44.Message;

@Slf4j
@Component
public class WorkflowRoutingHandler implements ApplicationListener<WorkflowEvent> {

    private static final String FLX_WORKFLOW = "FLX_WORKFLOW";

    @Override
    public void onApplicationEvent(WorkflowEvent event) {

        try{
            final Message message = event.getMessage();
            final String senderCompId = message.getHeader().getString( SenderCompID.FIELD );
            message.setString( FixConstants.FLX_TARGET_COMP_ID, senderCompId );

            Session.sendToTarget( message, FixSenderSession.FLX_SERVER.name(), FLX_WORKFLOW );

        } catch (SessionNotFound | FieldNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            log.error("Session not found");
        }
    }

}
