package com.flowlinx.fix.server.fix;

import com.flowlinx.fix.server.DB;
import com.flowlinx.fix.server.domain.ClientordidBuysideMapping;
import com.flowlinx.fix.server.domain.FixSessionExt;
import com.flowlinx.fix.server.repository.FixSessionExtRepository;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import com.flowlinx.fix.server.resource.FixSessionResource;
import com.flowlinx.fix.server.service.ClientordidBuysideMappingService;
import com.flowlinx.fix.server.service.QueuedMessageProcessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import quickfix.*;
import quickfix.field.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;


import java.time.format.DateTimeFormatter;


@Slf4j
@Component
public class ServerApplicationAdapter extends MessageCracker implements Application {

    @Autowired
    private FixSessionRouter router;

    @Autowired
    private FixSessionExtRepository fixSessionExtRepository;

    @Autowired
    private ClientordidBuysideMappingService clientordidBuysideMappingService;

    private final Map<SessionID, Integer> sessionSeqNumMap = new HashMap<>();

    @Autowired
    private QueuedMessageProcessor queuedMessageProcessor;

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


//        String sId = sessionId.toString().split(":")[1].replace("->", ".");
//        String fixVersion = sessionId.toString().split(":")[0];
//        FixSessionExt fixSessionExt = fixSessionExtRepository.findBySessionIdAndFixVersion(sId, fixVersion);

    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectTagValue, UnsupportedMessageType {
//        if (message.getHeader().getField(new MsgType()).getValue().equals("S")) {
            log.info("fromApp: " + System.nanoTime() + " " + message);
            log.info("<<<<<<<<<<<<<<<<<------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.." );
//        }
        crack( message, sessionId );
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
//        }
    }

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("onCreate: SessionId={}", sessionID);


//        String s= sessionID.toString();
//        String beginString = s.substring(0, s.indexOf(':'));
//        String simpleSessionName = s.substring(s.indexOf(':')+1, s.indexOf('-'))
//                + "."
//                + s.substring(s.indexOf('>')+1, s.length());
//        System.out.println("Session name ==>"+simpleSessionName);
//        String sender = simpleSessionName.substring(0,simpleSessionName.indexOf('.'));
//        String receiver = simpleSessionName.substring(simpleSessionName.indexOf('.')+1,simpleSessionName.length());
////        System.out.println("OnCreate"+ sessionID);
//        boolean isUpdated = DB.isSequenceNumberUpdated(beginString, sender, receiver);
//
//        if(isUpdated == true) {
//
//            DB.setIsUpdatedToFalse(beginString, sender, receiver);
//            String in = DB.getInboundSequenceNumber(beginString, sender, receiver);
//            String out = DB.getOutboundSequnceNumber(beginString, sender, receiver);
//
////            this.sessionID = sessionID;
//            // Set the next sender message sequence number
//            try {
//                System.out.println(":::session=====>>>>>>>>>"+Session.lookupSession(sessionID));
//                Session.lookupSession(sessionID).setNextSenderMsgSeqNum(Integer.parseInt(out));
//                Session.lookupSession(sessionID).setNextTargetMsgSeqNum(Integer.parseInt(in));
//                System.out.println(":::session=====>>>>>>>>>"+Session.lookupSession(sessionID));
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    @SneakyThrows
    @Override
    public void onLogon(SessionID sessionId) {

        log.info("onLogon: SessionId={}", sessionId);

        String sId = sessionId.toString().split(":")[1].replace("->", ".");
        String senderCompId = sessionId.toString().split(":")[1].split("->")[0];
        String targetCompId = sessionId.toString().split(":")[1].split("->")[1];
        String fixVersion = sessionId.toString().split(":")[0];
        FixSessionExt fixSessionExt = fixSessionExtRepository.findBySessionIdAndFixVersion(sId, fixVersion);

//        if (fixSessionExt == null) {
//            fixSessionExt = new FixSessionExt();
//        }
//
//        fixSessionExt.setSessionId(sId);
//        fixSessionExt.setSenderCompId(senderCompId);
//        fixSessionExt.setTargetCompId(targetCompId);
//        fixSessionExt.setConnectionType("");
        fixSessionExt.setSessionStatus("UP");
//        fixSessionExt.setConnectionType("Acceptor");
//        fixSessionExt.setFirmName("");
//        fixSessionExt.setStartTime("");
//        fixSessionExt.setEndTime("");
//        fixSessionExt.setIpAddress("");
//        if (fixSessionExt.getPort() == null)
//            fixSessionExt.setPort(0L);
//        fixSessionExt.setFixUser("");
//        fixSessionExt.setFixPassword("");
//        fixSessionExt.setHeartbeatInterval(0L);
//        fixSessionExt.setFixVersion(fixVersion);
//        fixSessionExt.setSenderSubId("");
//        fixSessionExt.setSenderLocationId("");
//        fixSessionExt.setTargetSubId("");
//        fixSessionExt.setTargetLocationId("");
//        fixSessionExt.setApplicationType("");
//        fixSessionExt.setDataDictionary("");
//        fixSessionExt.setCounterpartyType("");
//        fixSessionExt.setRoutingTag("");
//        fixSessionExt.setTimestamp(Timestamp.from(Instant.now()));
//        fixSessionExt.setUseDataDictionary("");
//        fixSessionExt.setInstance("");
//        fixSessionExt.setRegion("");
//        fixSessionExt.setHub("");
//        fixSessionExt.setTimeZone("");
//        fixSessionExt.setDisplayStartTime("");
//        fixSessionExt.setDisplayEndTime("");
//        fixSessionExt.setNormalization(false);
//        fixSessionExt.setLastUpdated(Timestamp.from(Instant.now()));
        fixSessionExtRepository.save(fixSessionExt);
        System.out.println(":::SESSION::::::: "+ fixSessionExt.getSessionId() + " " + fixSessionExt.getFixVersion() + " " + fixSessionExt.getSessionStatus());


        queuedMessageProcessor.processQueuedMessages();

    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("onLogout: SessionId={}", sessionId);

        String sId = sessionId.toString().split(":")[1].replace("->", ".");
        String fixVersion = sessionId.toString().split(":")[0];
        FixSessionExt fixSessionExt = fixSessionExtRepository.findBySessionIdAndFixVersion(sId, fixVersion);
        System.out.println(":::SESSION TIME: " + fixSessionExt.getSessionId() + " " + fixSessionExt.getStartTime() + " " + fixSessionExt.getEndTime());
        boolean isCurrentTimeInRange = isCurrentTimeInRange(fixSessionExt.getStartTime(), fixSessionExt.getEndTime());

        if (isCurrentTimeInRange) {
            fixSessionExt.setSessionStatus("DOWN");
        } else {
            fixSessionExt.setSessionStatus("SCHEDULED_DOWN");
        }

//        fixSessionExt.setSessionStatus("DOWN");
        fixSessionExtRepository.save(fixSessionExt);
        System.out.println(":::SESSION::::::: "+ fixSessionExt.getSessionId() + " " + fixSessionExt.getFixVersion() + " " + fixSessionExt.getSessionStatus() + " " + fixSessionExt.getStartTime() + " " + fixSessionExt.getEndTime());
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
    public void executionReport(quickfix.fix44.Reject message, SessionID sessionID) {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        log.info( "sender={}, target={}", sessionID.getSenderCompID(), sessionID.getTargetCompID() );
        log.info("<<<<<<<<<<<<<<<<<<<<<<<-------------------------------------------------.." );
        router.route( message, sessionID );
    }

    @Handler
    public void executionReport(quickfix.fix42.Reject message, SessionID sessionID) {
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
    public void heartbeat(quickfix.fix42.Heartbeat message, SessionID sessionID) {
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

//    @Handler
//    public void messageHandler(Message message, SessionID sessionID) {
//        router.route( message, sessionID );
//    }

    @Handler
    public void multilegOrder(quickfix.fix44.NewOrderMultileg message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void multilegOrderCancelReplaceRequest(quickfix.fix44.MultilegOrderCancelReplaceRequest message, SessionID sessionID) {
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
    public void orderCancelReject(quickfix.fix44.OrderCancelReject message, SessionID sessionID) {
        router.route( message, sessionID );
    }

    @Handler
    public void orderCancelReject(quickfix.fix42.OrderCancelReject message, SessionID sessionID) {
        router.route( message, sessionID );
    }

//    class MyThread1 extends Thread{
//        public void run(){
//            System.out.println("MyThread1_________");
//        }
//    }
//
//    private Integer getLastKnownSequenceNumber(SessionID sessionId) {
//        // Retrieve the sequence number from your storage
//        return sessionSeqNumMap.get(sessionId);
//    }

    public static boolean isCurrentTimeInRange(String startTime, String endTime) {
        // Parse the input time strings to LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime start = LocalTime.parse(startTime, timeFormatter);
        LocalTime end = LocalTime.parse(endTime, timeFormatter);

        // Get the current time in GMT
        ZonedDateTime nowGMT = ZonedDateTime.now(ZoneId.of("GMT"));
        LocalTime currentTime = nowGMT.toLocalTime();

        // Check if the current time falls within the range [start, end)
        // Note: This assumes start <= end. For ranges that might wrap around midnight, further logic is needed.
        if (start.isBefore(end)) {
            return !currentTime.isBefore(start) && currentTime.isBefore(end);
        } else {
            // Handle the case where the range wraps around midnight
            return !currentTime.isBefore(start) || currentTime.isBefore(end);
        }
    }
}

//32540506818434 - 32538900464028