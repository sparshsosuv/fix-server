package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.QueuedMessage;
import com.flowlinx.fix.server.repository.QueuedMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.*;
import quickfix.field.BeginString;

import java.util.List;

@Slf4j
@Service
public class QueuedMessageProcessor {

    @Autowired
    private QueuedMessageRepository queuedMessageRepository;

//    @Scheduled(fixedRate = 600000)
//    public void processQueuedMessages() {
//        log.info("Processing Queued Messages......");
//        List<QueuedMessage> queuedMessages = queuedMessageRepository.findByProcessedFalse();
//
//        for (QueuedMessage queuedMessage : queuedMessages) {
//            String beginString = java.util.Arrays.stream(queuedMessage.getMessage().split(""))
//                    .filter(field -> field.startsWith("8="))
//                    .map(field -> field.substring(2))
//                    .findFirst()
//                    .orElse("Not Found");
//            if (beginString.equals("Not Found")) {
//                continue;
//            }
//            String sender = queuedMessage.getSender();
//            String receiver = queuedMessage.getReceiver();
//            SessionID buySideSessionId = new SessionID(beginString, sender, receiver);
//            if (isBuySideUp(buySideSessionId) && !queuedMessage.isProcessed()) {
//                try {
//                    Message message = new Message(queuedMessage.getMessage());
//                    sendToBuySide(message, queuedMessage.getClientOrderId(), sender, receiver);
//                    queuedMessage.setProcessed(true);
//                    queuedMessageRepository.save(queuedMessage);
//                } catch (InvalidMessage | SessionNotFound e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public void processQueuedMessages(String receiver) {
        log.info("Processing Queued Messages......: " + receiver);
        List<QueuedMessage> queuedMessages = queuedMessageRepository.findByProcessedFalseAndReceiver(receiver);

        for (QueuedMessage queuedMessage : queuedMessages) {
            String beginString = java.util.Arrays.stream(queuedMessage.getMessage().split(""))
                    .filter(field -> field.startsWith("8="))
                    .map(field -> field.substring(2))
                    .findFirst()
                    .orElse("Not Found");
            if (beginString.equals("Not Found")) {
                continue;
            }
            String sender = queuedMessage.getSender();
//            String receiver = queuedMessage.getReceiver();
            SessionID buySideSessionId = new SessionID(beginString, sender, receiver);
            if (isBuySideUp(buySideSessionId) && !queuedMessage.isProcessed()) {
                try {
                    Message message = new Message(queuedMessage.getMessage());
                    sendToBuySide(message, queuedMessage.getClientOrderId(), sender, receiver);
                    queuedMessage.setProcessed(true);
                    queuedMessageRepository.save(queuedMessage);
                } catch (InvalidMessage | SessionNotFound e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isBuySideUp(SessionID sessionID) {
        System.out.println("---BUYSIDE: " + sessionID);
        System.out.println("Session.lookupSession(sessionID): " + Session.lookupSession(sessionID));
        System.out.println("Session.lookupSession(sessionID).isLoggedOn(): " + Session.lookupSession(sessionID).isLoggedOn());
        return Session.lookupSession(sessionID) != null && Session.lookupSession(sessionID).isLoggedOn();
    }

    private void sendToBuySide(Message message, String clientOrderId, String sender, String receiver) throws SessionNotFound {
        // Implement logic to send the message to the buy side
        Session.sendToTarget(message, sender, receiver);
    }
}
