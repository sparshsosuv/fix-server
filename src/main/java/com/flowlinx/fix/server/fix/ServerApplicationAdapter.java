package com.flowlinx.fix.server.fix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.*;

@Slf4j
@Component
public class ServerApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private FixSessionRouter router;

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
            throws FieldNotFound, IncorrectTagValue, UnsupportedMessageType {
        log.info("<<<<<<<<<<<<<<<<<------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.." );
        crack( message, sessionId );
        log.info("<<<<<<<<<<<<<<<<<------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.." );
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
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        log.info( "sender={}, target={}", sessionID.getSenderCompID(), sessionID.getTargetCompID() );
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        router.route( message, sessionID );
    }

    @Handler
    public void executionReport(quickfix.fix44.Reject message, SessionID sessionID) {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        log.info( "sender={}, target={}", sessionID.getSenderCompID(), sessionID.getTargetCompID() );
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        router.route( message, sessionID );
    }

    @Handler
    public void heartbeat(quickfix.fix44.Heartbeat message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void newOrderHandler(quickfix.fix44.NewOrderSingle message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void replaceOrder(quickfix.fix44.OrderCancelReplaceRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void cancelOrder(quickfix.fix44.OrderCancelRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }

}