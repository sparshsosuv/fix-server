package com.flowlinx.fix.server.message.handlers;

import com.flowlinx.fix.server.type.FixSession;
import com.flowlinx.fix.server.message.events.ExecutionReportEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.SenderCompID;

import java.util.Arrays;

@Component
public class ExecutionReportHandler implements ApplicationListener<ExecutionReportEvent> {

    private static final Logger log = LoggerFactory.getLogger(ExecutionReportHandler.class);

    @Override
    public void onApplicationEvent(ExecutionReportEvent executionReportEvent) {
        try {
            String sender = executionReportEvent.getMessage().getHeader().getString(SenderCompID.FIELD);
            Arrays.stream(FixSession.values())
                    .filter(fixSession -> !sender.equalsIgnoreCase(fixSession.name()))
                    .forEach(fixSession -> sendToTarget(executionReportEvent, fixSession));
        } catch (FieldNotFound ex){
            log.error("FieldNotFound on execution report event");
            ex.printStackTrace();
        }

    }

    private boolean sendToTarget(ExecutionReportEvent executionReportEvent, FixSession fixSession) {
        boolean sent = false;
        try {
            sent = Session.sendToTarget(executionReportEvent.getMessage(), FixSession.SERVER.name(), fixSession.name());
        } catch (SessionNotFound ex){
            log.error("FieldNotFound on execution report event");
            ex.printStackTrace();
        }
        return sent;
    }
}
