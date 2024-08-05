package com.flowlinx.fix.server.fix;

import com.flowlinx.fix.server.domain.FixSessionExt;
import com.flowlinx.fix.server.repository.FixSessionExtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.*;

@Slf4j
@Component
public class ClientApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private FixSessionRouter router;

    @Autowired
    private FixSessionExtRepository fixSessionExtRepository;


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

        log.info("<<<<<<<<<<<<<<<<<------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.." );
        crack(message, sessionId);
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

        String sId = sessionId.toString().split(":")[1].replace("->", ".");
        String senderCompId = sessionId.toString().split(":")[1].split("->")[0];
        String targetCompId = sessionId.toString().split(":")[1].split("->")[1];
        String fixVersion = sessionId.toString().split(":")[0];
        FixSessionExt fixSessionExt = fixSessionExtRepository.findBySessionIdAndFixVersion(sId, fixVersion);
        fixSessionExt.setSessionStatus("UP");
        fixSessionExtRepository.save(fixSessionExt);
        System.out.println(":::SESSION::::::: "+ fixSessionExt.getSessionId() + " " + fixSessionExt.getFixVersion() + " " + fixSessionExt.getSessionStatus());

    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("onLogout: SessionId={}", sessionId);

        String sId = sessionId.toString().split(":")[1].replace("->", ".");
        String fixVersion = sessionId.toString().split(":")[0];
        FixSessionExt fixSessionExt = fixSessionExtRepository.findBySessionIdAndFixVersion(sId, fixVersion);
        fixSessionExt.setSessionStatus("DOWN");
        fixSessionExtRepository.save(fixSessionExt);
        System.out.println(":::SESSION::::::: "+ fixSessionExt.getSessionId() + " " + fixSessionExt.getFixVersion() + " " + fixSessionExt.getSessionStatus());
    }

    @Handler
    public void executionReport(quickfix.fix44.ExecutionReport message, SessionID sessionID) {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        log.info( "sender={}, target={}", sessionID.getSenderCompID(), sessionID.getTargetCompID() );
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        router.route( message, sessionID );
    }

    @Handler
    public void executionReport(quickfix.fix42.ExecutionReport message, SessionID sessionID) {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        log.info( "sender={}, target={}", sessionID.getSenderCompID(), sessionID.getTargetCompID() );
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        router.route( message, sessionID );
    }

    @Handler
    public void newOrderHandler(quickfix.fix44.NewOrderSingle message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void newOrderHandler(quickfix.fix42.NewOrderSingle message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void replaceOrder(quickfix.fix44.OrderCancelReplaceRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void replaceOrder(quickfix.fix42.OrderCancelReplaceRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void cancelOrder(quickfix.fix44.OrderCancelRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void cancelOrder(quickfix.fix42.OrderCancelRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }


    @Handler
    public void orderCancelReject(quickfix.fix44.OrderCancelReject message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void orderCancelReject(quickfix.fix42.OrderCancelReject message, SessionID sessionID) {
        router.route( message, sessionID );
    }


    @Handler
    public void quoteRequestOrder(quickfix.fix44.QuoteRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void quoteOrder(quickfix.fix44.Quote message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void quoteRequestOrder(quickfix.fix42.QuoteRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void quoteOrder(quickfix.fix42.Quote message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void multilegOrder(quickfix.fix44.NewOrderMultileg message, SessionID sessionID) {
        router.route( message, sessionID );
    }


}