package com.flowlinx.fix.server.fix;

import com.flowlinx.fix.server.domain.ClientordidBuysideMapping;
import com.flowlinx.fix.server.domain.FixSessionExt;
import com.flowlinx.fix.server.repository.FixSessionExtRepository;
import com.flowlinx.fix.server.service.ClientordidBuysideMappingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;

import java.util.Optional;

@Slf4j
@Component
public class ClientApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private FixSessionRouter router;

    @Autowired
    private FixSessionExtRepository fixSessionExtRepository;

    @Autowired
    private ClientordidBuysideMappingService clientordidBuysideMappingService;


    @SneakyThrows
    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
        if (message.getHeader().getString(MsgType.FIELD).equals("3")) {
            Long msgSeqNum = (long) message.getInt(RefSeqNum.FIELD);
            String sender = message.getHeader().getString(TargetCompID.FIELD);
            String target = message.getHeader().getString(SenderCompID.FIELD);
            String msgType = message.getString(RefMsgType.FIELD);
            String fixVersion = message.getHeader().getString(BeginString.FIELD);
            System.out.println("===============");
            System.out.println(msgSeqNum);
            System.out.println(sender);
            System.out.println(target);
            System.out.println(msgType);
            System.out.println(fixVersion);
            System.out.println("===============\n");
            Optional<ClientordidBuysideMapping> opt = clientordidBuysideMappingService.findByMsgTypeAndMsgSeqNumAndSenderAndTargetAndFixVersion(msgType, msgSeqNum, sender, target, fixVersion);
            System.out.println("opt: " + opt);
            if (opt.isPresent()) {
                String buySideSession = opt.get().getBuySideSession();
                String buySideSender = buySideSession.substring(buySideSession.indexOf('.') + 1, buySideSession.length());
                String buySideReceiver = buySideSession.substring(0, buySideSession.indexOf('.'));
                System.out.println(buySideSender + " " + buySideReceiver);
                message.setString(Text.FIELD, message.getString(Text.FIELD) + "_BrokerReject");
                Session.sendToTarget(message, buySideSender, buySideReceiver);
            }
        }
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

//        if (message.getHeader().getField(new MsgType()).getValue().equals("S")) {
            log.info("fromApp: " + System.nanoTime() + " " + message);
            log.info("<<<<<<<<<<<<<<<<<------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
//        }
        crack(message, sessionId);
//        if (message.getHeader().getField(new MsgType()).getValue().equals("S")) {
            log.info("<<<<<<<<<<<<<<<<<------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
//        }
    }

    @SneakyThrows
    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
//        if (message.getHeader().getField(new MsgType()).getValue().equals("S")) {
        Optional<ClientordidBuysideMapping> opt = clientordidBuysideMappingService.findByClientOrderId(message.getString(ClOrdID.FIELD));
        if (opt.isPresent()) {
            ClientordidBuysideMapping item = opt.get();
            System.out.println("--------------------\n" + item);
            System.out.println(message.toString());
            item.setMessage(message.toString());
            item.setMsgType(message.getHeader().getString(MsgType.FIELD));
            item.setMsgSeqNum((long) message.getHeader().getInt(MsgSeqNum.FIELD));
            item.setSender(message.getHeader().getString(SenderCompID.FIELD));
            item.setTarget(message.getHeader().getString(TargetCompID.FIELD));
            item.setFixVersion(message.getHeader().getString(BeginString.FIELD));
            System.out.println("--------------------\n" + item + "--------------------\n");
            clientordidBuysideMappingService.save(item);
        }
            log.info("toApp: {} Message={}, SessionId={}", System.nanoTime(), message, sessionId);
            log.info("<<<<<<<<<<<<<<<<<------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.." );
//        }
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

    @Handler
    public void multilegOrderCancelReplaceRequest(quickfix.fix44.MultilegOrderCancelReplaceRequest message, SessionID sessionID) {
        router.route( message, sessionID );
    }



}
