package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.domain.ClientordidBuysideMapping;
import com.flowlinx.fix.server.domain.QueuedMessage;
import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.repository.QueuedMessageRepository;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import com.flowlinx.fix.server.resource.FixSessionResource;
import com.flowlinx.fix.server.service.ClientordidBuysideMappingService;
import com.flowlinx.fix.server.service.NormalizationService;
import com.flowlinx.fix.server.service.RuleService;
import com.flowlinx.fix.server.utils.FixConstants;
import com.flowlinx.fix.server.message.event.ClientEvent;
import com.flowlinx.fix.server.type.FixTargetSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.BeginString;
import quickfix.field.MsgType;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class ClientEventHandler implements ApplicationListener<ClientEvent> {

    @Autowired
    private ClientordidBuysideMappingService clientordidBuysideMappingService;

    @Autowired(required = false)
    private ThreadedSocketAcceptor acceptor;

    @Autowired(required = false)
    private ThreadedSocketInitiator initiator;

    @Autowired
    private QueuedMessageRepository queuedMessageRepository;

    @Autowired
    private RuleService ruleService;


    @Override
    public void onApplicationEvent(ClientEvent event) {
        Message message = event.getMessage();
//        List<Map<String, Object>> rules;
//        try {
//            rules = ruleService.getAllRules();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        NormalizationService normalizationService = new NormalizationService();
        try {
            if (message.isSetField(FixConstants.FLX_TARGET_COMP_ID)) {
                System.out.println("---------------MESSAGE ClientEventHandler WFLW---------------\n" + message);
                final String flxTargetCompId = message.getString(FixConstants.FLX_TARGET_COMP_ID);
                message.removeField(FixConstants.FLX_TARGET_COMP_ID);
                final Optional<FixTargetSession> opt = FixTargetSession.from(flxTargetCompId);
                String sender = opt.get().getSender().name();
                String receiver = flxTargetCompId;
                boolean isMsgType8 = message.getHeader().getField(new MsgType()).getValue().equals("8");
                boolean isMsgTypeS = message.getHeader().getField(new MsgType()).getValue().equals("S");
                boolean isMsgTypeR = message.getHeader().getField(new MsgType()).getValue().equals("R");
                String clientOrderId = isMsgTypeS ? message.getString(131) : message.getString(11);

                if (opt.isPresent()) {
                    System.out.println("-----Normalization check-----");

                    final SessionID sessionId = event.getSessionId();
                    final Properties properties = acceptor.getSessions().contains(sessionId)
                            ? acceptor.getSettings().getSessionProperties(sessionId)
                            : initiator.getSettings().getSessionProperties(sessionId);

                    if (properties.getProperty("Normalization") != null && properties.getProperty("Normalization").equals("Y")) {
                        List<String> clientIds = properties.getProperty("NormalizationClientIds") != null
                                ? Arrays.asList(properties.getProperty("NormalizationClientIds").split(","))
                                : null;
                        String clientId = message.getHeader().isSetField(115)
                                ? message.getHeader().getString(115)
                                : message.getHeader().isSetField(50)
                                ? message.getHeader().getString(50)
                                : null;

                        System.out.println("clientId: " + clientId);
                        if (clientIds == null || (clientId != null && clientIds.contains(clientId))) {
                            System.out.println(clientIds);
                            System.out.println(message);
//                            List<Map<String, Object>> rules;
//                            try {
//                                rules = ruleService.getAllRules();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            message = normalizationService.applyNormalizationRules(ruleService.getAllRules(), message);
                            System.out.println(message);
                            System.out.println("-----Normalized-----");
                        }
                    }

                    String beginString = message.getHeader().getField(new BeginString()).getValue();
                    SessionID buySideSessionId = new SessionID(beginString, sender, receiver);
                    if (isBuySideUp(buySideSessionId)) {
                        Session.sendToTarget(message, sender, receiver);
                    } else {
                        queueMessage(clientOrderId, message, sender, receiver);
                    }

//                    Session.sendToTarget(message, opt.get().getSender().name(), flxTargetCompId);
                }

            } else {
                System.out.println("---------------MESSAGE ClientEventHandler---------------\n" + message);
                System.out.println("orderMessage.getString(49): " + message.getHeader().getString(49));
                System.out.println("orderMessage.getString(56): " + message.getHeader().getString(56));
                boolean isMsgType8 = message.getHeader().getField(new MsgType()).getValue().equals("8");
                boolean isMsgTypeS = message.getHeader().getField(new MsgType()).getValue().equals("S");
                boolean isMsgTypeR = message.getHeader().getField(new MsgType()).getValue().equals("R");
                String clientOrderId = isMsgTypeS ? message.getString(131) : message.getString(11);

                final Optional<ClientordidBuysideMapping> opt = clientordidBuysideMappingService.findByClientOrderId(clientOrderId);
                if (opt.isPresent()) {
                    String buySideSession = opt.get().getBuySideSession();
                    String sender = buySideSession.substring(buySideSession.indexOf('.') + 1, buySideSession.length());
                    String receiver = buySideSession.substring(0, buySideSession.indexOf('.'));
                    System.out.println(sender + " " + receiver);


                    System.out.println("-----Normalization check-----");

                    final SessionID sessionId = event.getSessionId();
                    final Properties properties = acceptor.getSessions().contains(sessionId)
                            ? acceptor.getSettings().getSessionProperties(sessionId)
                            : initiator.getSettings().getSessionProperties(sessionId);

                    if (properties.getProperty("Normalization") != null && properties.getProperty("Normalization").equals("Y")) {
                        List<String> clientIds = properties.getProperty("NormalizationClientIds") != null
                                ? Arrays.asList(properties.getProperty("NormalizationClientIds").split(","))
                                : null;
                        String clientId = message.getHeader().isSetField(115)
                                ? message.getHeader().getString(115)
                                : message.getHeader().isSetField(50)
                                ? message.getHeader().getString(50)
                                : null;

                        System.out.println(clientId);
                        if (clientIds == null || (clientId != null && clientIds.contains(clientId))) {
                            System.out.println(clientIds);
                            System.out.println(message);
                            message = normalizationService.applyNormalizationRules(ruleService.getAllRules(), message);
                            System.out.println(message);
                            System.out.println("-----Normalized-----");
                        }
                    }

                    String beginString = message.getHeader().getField(new BeginString()).getValue();
                    SessionID buySideSessionId = new SessionID(beginString, sender, receiver);
                    if (isBuySideUp(buySideSessionId)) {
                        Session.sendToTarget(message, sender, receiver);
                    } else {
                        log.info("The BUYSIDE " + buySideSessionId + " is down");
                        queueMessage(clientOrderId, message, sender, receiver);
                    }
                } else {
                    log.info("!!!!!!!!!Buyside routing doesn't exist");
                }
            }
        } catch (FieldNotFound | SessionNotFound | IncorrectTagValue | ConfigError | InvalidMessage e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isBuySideUp(SessionID sessionID) throws FieldNotFound {
        System.out.println("BUYSIDE: " + sessionID);
        System.out.println("Session.lookupSession(sessionID): " + Session.lookupSession(sessionID));
        System.out.println("Session.lookupSession(sessionID).isLoggedOn(): " + Session.lookupSession(sessionID).isLoggedOn());
        System.out.println("Session.lookupSession(sessionID).isEnabled(): " + Session.lookupSession(sessionID).isEnabled());

        return Session.lookupSession(sessionID) != null && Session.lookupSession(sessionID).isLoggedOn();
    }

    private void queueMessage(String clientOrderId, Message message, String sender, String receiver) {
        QueuedMessage queuedMessage = new QueuedMessage();
        queuedMessage.setClientOrderId(clientOrderId);
        queuedMessage.setMessage(message.toString());
        queuedMessage.setProcessed(false);
        queuedMessage.setSender(sender);
        queuedMessage.setReceiver(receiver);
        queuedMessageRepository.save(queuedMessage);
        log.info("Message queued: {}", queuedMessage);
    }
}
