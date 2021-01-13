package com.hes.zf.fix.server;

import com.hes.zf.fix.server.message.events.CreateNewOrderEvent;
import com.hes.zf.fix.server.message.events.ExecutionReportEvent;
import com.hes.zf.fix.server.message.events.OrderCancelReplaceEvent;
import com.hes.zf.fix.server.message.events.OrderCancelRequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.SenderCompID;

@Component
public class ServerApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private ApplicationEventPublisher publisher;

    private static final Logger log = LoggerFactory.getLogger(ServerApplicationAdapter.class);

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("\nfromAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

        message.getHeader().getString(SenderCompID.FIELD);
        crack(message, sessionId);

    }

    @Handler
    public void executionReport(quickfix.fix44.ExecutionReport message, SessionID sessionID) throws FieldNotFound {
        log.info("\nexecutionReport: SessionId={}", sessionID);
        publisher.publishEvent(new ExecutionReportEvent(this,message));

    }

    @Handler
    public void newOrderHandler(quickfix.fix44.NewOrderSingle message, SessionID sessionID) throws FieldNotFound {
        log.info("\nnewOrderHandler: SessionId={}", sessionID);
        publisher.publishEvent(new CreateNewOrderEvent(this,message));

    }

    @Handler
    public void replaceOrder(quickfix.fix44.OrderCancelReplaceRequest message, SessionID sessionID) throws FieldNotFound {
        log.info("\nreplaceOrderHandler: SessionId={} Message={}", sessionID, message);
        publisher.publishEvent(new OrderCancelReplaceEvent(this,message));

    }

    @Handler
    public void cancelOrder(quickfix.fix44.OrderCancelRequest message, SessionID sessionID) throws FieldNotFound {
        log.info("\ncancelOrderHandler: SessionId={} Message={}", sessionID, message);
        publisher.publishEvent(new OrderCancelRequestEvent(this,message));

    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("\nonCreate: SessionId={}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("\nonLogon: SessionId={}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("\nonLogout: SessionId={}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("\ntoAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("\ntoApp: Message={}, SessionId={}", message, sessionId);
    }
}