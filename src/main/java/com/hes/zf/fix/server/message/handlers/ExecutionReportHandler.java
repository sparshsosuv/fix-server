package com.hes.zf.fix.server.message.handlers;

import com.hes.zf.fix.server.message.events.ExecutionReportEvent;
import com.hes.zf.fix.server.type.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.SenderCompID;

@Component
public class ExecutionReportHandler implements ApplicationListener<ExecutionReportEvent> {

    private static final Logger log = LoggerFactory.getLogger(ExecutionReportHandler.class);

    @Override
    public void onApplicationEvent(ExecutionReportEvent executionReportEvent) {
        try {
            String sender = executionReportEvent.getMessage().getHeader().getString(SenderCompID.FIELD);
            if(FixSession.CLIENT.name().equalsIgnoreCase(sender)) {
                Session.sendToTarget( executionReportEvent.getMessage(), FixSession.SERVER.name(), FixSession.WORKFLOW.name() );
            } else {
                Session.sendToTarget( executionReportEvent.getMessage(), FixSession.SERVER.name(), FixSession.CLIENT.name() );
            }
        } catch (FieldNotFound | SessionNotFound ex){
            log.error("FieldNotFound on execution report event");
            ex.printStackTrace();
        }

    }
}
