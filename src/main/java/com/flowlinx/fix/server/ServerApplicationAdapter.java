package com.flowlinx.fix.server;

import com.flowlinx.fix.server.message.events.CreateNewOrderEvent;
import com.flowlinx.fix.server.message.events.ExecutionReportEvent;
import com.flowlinx.fix.server.message.events.OrderCancelReplaceEvent;
import com.flowlinx.fix.server.message.events.OrderCancelRequestEvent;
import com.flowlinx.fix.server.persistence.FixMessage;
import com.flowlinx.fix.server.persistence.FixMessageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.SenderCompID;

import java.text.MessageFormat;
import java.util.Date;

@Component
public class ServerApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private FixMessageRepo fixMessageRepo;

    private static final Logger log = LoggerFactory.getLogger(ServerApplicationAdapter.class);

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

        log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
        fixMessageRepo.save( new FixMessage( new Date() , message.toString() ) );
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin: Message={}, SessionId={}", message, sessionId);
        fixMessageRepo.save( new FixMessage( new Date() , message.toString() ) );
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

        message.getHeader().getString(SenderCompID.FIELD);
        crack(message, sessionId);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("toApp: Message={}, SessionId={}", message, sessionId);
    }    

    @Handler
    public void executionReport(quickfix.fix44.ExecutionReport message, SessionID sessionID) throws FieldNotFound {
        log.info("executionReport: SessionId={}", sessionID);
        publisher.publishEvent( new ExecutionReportEvent(this,message) );
        fixMessageRepo.save( new FixMessage( new Date() , message.toString() ) );
    }

    @Handler
    public void newOrderHandler(quickfix.fix44.NewOrderSingle message, SessionID sessionID) throws FieldNotFound {
        log.info("newOrderHandler: SessionId={}", sessionID);
        publisher.publishEvent( new CreateNewOrderEvent(this,message) );
        fixMessageRepo.save( new FixMessage( new Date() , message.toString() ) );
    }

    @Handler
    public void replaceOrder(quickfix.fix44.OrderCancelReplaceRequest message, SessionID sessionID) throws FieldNotFound {
        log.info("replaceOrderHandler: SessionId={} Message={}", sessionID, message);
        publisher.publishEvent( new OrderCancelReplaceEvent(this,message) );
        fixMessageRepo.save( new FixMessage( new Date() , message.toString() ) );
    }

    @Handler
    public void cancelOrder(quickfix.fix44.OrderCancelRequest message, SessionID sessionID) throws FieldNotFound {
        log.info("cancelOrderHandler: SessionId={} Message={}", sessionID, message);
        publisher.publishEvent(new OrderCancelRequestEvent(this,message));
        fixMessageRepo.save( new FixMessage( new Date() , message.toString() ) );
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("onCreate: SessionId={}", sessionId);
        fixMessageRepo.save( new FixMessage( new Date(), MessageFormat.format( "onCreate: SessionId={0}", sessionId ) ) );
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("onLogon: SessionId={}", sessionId);
        fixMessageRepo.save( new FixMessage( new Date(), MessageFormat.format( "onLogon: SessionId={0}", sessionId ) ) );
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("onLogout: SessionId={}", sessionId);
        fixMessageRepo.save( new FixMessage( new Date(), MessageFormat.format( "onLogout: SessionId={0}", sessionId ) ) );
    }

}