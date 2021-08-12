package com.flowlinx.fix.server;

import com.flowlinx.fix.server.message.event.ClientEvent;
import com.flowlinx.fix.server.message.event.WorkflowEvent;
import com.flowlinx.fix.server.type.FixTargetSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import quickfix.*;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class ClientApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        crack(message, sessionId);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("toApp: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("onCreate: SessionId={}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("onLogon: SessionId={}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("onLogout: SessionId={}", sessionId);
    }

    @Handler
    public void executionReport(quickfix.fix44.ExecutionReport message, SessionID sessionID) {
        publishEvent( message, sessionID );
    }

    @Handler
    public void newOrderHandler(quickfix.fix44.NewOrderSingle message, SessionID sessionID) {
        publishEvent( message, sessionID );
    }

    @Handler
    public void replaceOrder(quickfix.fix44.OrderCancelReplaceRequest message, SessionID sessionID) {
        publishEvent( message, sessionID );
    }

    @Handler
    public void cancelOrder(quickfix.fix44.OrderCancelRequest message, SessionID sessionID) {
        publishEvent( message, sessionID );
    }

    private void publishEvent(quickfix.fix44.Message message, SessionID sessionID) {

        final Optional<FixTargetSession> optSession = Arrays.stream(FixTargetSession.values())
                .filter(t -> t.name().equalsIgnoreCase( sessionID.getTargetCompID() ) ).findFirst();

        if( optSession.isPresent() ){
            publisher.publishEvent( new WorkflowEvent( message, sessionID ) );

        } else {
            publisher.publishEvent( new ClientEvent( message, sessionID ) );
        }

    }
}