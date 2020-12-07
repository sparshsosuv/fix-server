package io.allune.quickfixj.spring.boot.starter.examples.server;

import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.OrderRepresentation;
import io.allune.quickfixj.spring.boot.starter.examples.server.service.ExecutionReportRepresentation;
import io.allune.quickfixj.spring.boot.starter.examples.server.service.ExecutionReportService;
import io.allune.quickfixj.spring.boot.starter.examples.server.service.OrderService;
import io.allune.quickfixj.spring.boot.starter.examples.server.service.SingleOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.SenderCompID;

@Component
public class ServerApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private SingleOrderMapper mapper;
    @Autowired
    private OrderService service;

    private static final Logger log = LoggerFactory.getLogger(ServerApplicationAdapter.class);

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

        message.getHeader().getString(SenderCompID.FIELD);
        crack(message, sessionId);

    }

    @Handler
    public void newOrderHandler(quickfix.fix44.NewOrderSingle message, SessionID sessionID) throws FieldNotFound {
        log.info("newOrderHandler: SessionId={}", sessionID);
        OrderRepresentation order = mapper.messageToOrder(message);
        service.createOrder(order);
        ExecutionReportService.send(message);
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

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("toApp: Message={}, SessionId={}", message, sessionId);
    }
}