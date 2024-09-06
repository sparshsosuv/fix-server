package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.domain.ClientordidBuysideMapping;
import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.message.event.RouteEvent;
import com.flowlinx.fix.server.representation.FixSessionIdRepresentation;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import com.flowlinx.fix.server.resource.FixSessionResource;
import com.flowlinx.fix.server.service.ClientordidBuysideMappingService;
import com.flowlinx.fix.server.service.NormalizationService;
import com.flowlinx.fix.server.service.RuleService;
import com.flowlinx.fix.server.type.FixTargetSession;
import com.flowlinx.fix.server.utils.AppUtils;
import com.flowlinx.fix.server.utils.FixConstants;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import quickfix.DefaultMessageFactory;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.field.*;
import quickfix.fix42.Reject;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Component
public class RouteEventHandler implements ApplicationListener<RouteEvent> {

    @Autowired
    private ClientordidBuysideMappingService clientordidBuysideMappingService;

    @Autowired
    private FixSessionResource fixSessionResource;

    private DefaultMessageFactory messageFactory = new DefaultMessageFactory();

    @Autowired
    private RuleService ruleService;

    @Override
    public void onApplicationEvent(RouteEvent event) {

        try{
            Message message = event.getMessage();
            final RoutingTable route = event.getRoutingTable();
            final String senderCompID =  AppUtils.getString( message.getHeader(), SenderCompID.FIELD );
            final String targetCompID =  AppUtils.getString( message.getHeader(), TargetCompID.FIELD );

            Session session = Session.lookupSession(new SessionID(
                    message.getHeader().getString(BeginString.FIELD),
                    route.getSenderCompID(),
                    route.getTargetCompID()
            ));

            if (session == null || !session.isEnabled() || !session.isLoggedOn()) {
                System.out.println(session == null ? "Session not found." : "The session is not heartbeating.");

                final String server = route.getSenderCompID();

                Message rejectionReport = messageFactory.create(message.getHeader().getString(BeginString.FIELD), MsgType.EXECUTION_REPORT);
                rejectionReport.getHeader().setField(new SenderCompID(message.getHeader().getString(49)));
                rejectionReport.getHeader().setField(new TargetCompID(message.getHeader().getString(56)));
                rejectionReport.getHeader().setField(new SendingTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime()));
                rejectionReport.getHeader().setField(new MsgSeqNum(message.getHeader().getInt(34) + 1));

                rejectionReport.setField(new ClOrdID(message.getString(11)));
                rejectionReport.setField(new ExecID("E" + message.getString(11)));
                rejectionReport.setField(new OrdStatus(OrdStatus.REJECTED));
                rejectionReport.setField(new Text(StringUtils.replace( route.getTargetCompID() + "-" + message.getHeader().getString(BeginString.FIELD) + " session is currently down", " ", "_" )));
                rejectionReport.setField(new LastShares());
                rejectionReport.setField(new Side(message.getChar(54)));
                rejectionReport.setField(new Symbol(message.getString(55)));
                rejectionReport.setField(new ExecType(ExecType.REJECTED));
                rejectionReport.setField(new OrderID("O" + message.getString(11)));
                if(!message.getHeader().getString(BeginString.FIELD).contains("4.4"))
                    rejectionReport.setField(new ExecTransType(ExecTransType.NEW));
                rejectionReport.setField(new OrderQty(message.getInt(38)));
                rejectionReport.setField(new LeavesQty(message.getInt(38)));
                rejectionReport.setField(new AvgPx());
                rejectionReport.setField(new CumQty());

                Session.sendToTarget( rejectionReport, targetCompID, senderCompID );
                return;
            } else {
                System.out.println("The session is heartbeating.");
            }

            
            List<Map<String, Object>> rules;
            try {
                rules = ruleService.getAllRules();
            } catch (IOException e) {
                e.printStackTrace();
            }




//            message.getHeader().setField( new OnBehalfOfCompID( senderCompID ) );

            System.out.println("---------------MESSAGE---------------\n" + message);
//            System.out.println("orderMessage.getString(11): " + message.getString(11));
            System.out.println("orderMessage.getString(49): " + message.getHeader().getString(49));
            System.out.println("orderMessage.getString(56): " + message.getHeader().getString(56));
            System.out.println(route.getSenderCompID() + " " + route.getTargetCompID());

            boolean isMsgType8 = message.getHeader().getField(new MsgType()).getValue().equals("8");
            boolean isMsgTypeS = message.getHeader().getField(new MsgType()).getValue().equals("S");
            boolean isMsgTypeR = message.getHeader().getField(new MsgType()).getValue().equals("R");
            String clientOrderId = isMsgTypeR ? message.getString(131) : message.getString(11);
            String buySideSession = message.getHeader().getString(49) + "." + message.getHeader().getString(56);
            ClientordidBuysideMapping item = new ClientordidBuysideMapping();
            item.setClientOrderId(clientOrderId);
            item.setBuySideSession(buySideSession);
            clientordidBuysideMappingService.save(item);

            NormalizationService normalizationService = new NormalizationService();
            System.out.println("-----Normalization check-----");
            FixSessionRepresentation fixSessionRepresentation = fixSessionResource.getSession("Acceptor", event.getSessionId().getTargetCompID()).getBody();
            if (fixSessionRepresentation.isNormalization()) {
                List<String> clientIds = fixSessionRepresentation.getNormalizationClientIds() != null
                        ? Arrays.asList(fixSessionRepresentation.getNormalizationClientIds().split(","))
                        : null;
                String clientId = message.getHeader().isSetField(115)
                        ? message.getHeader().getString(115)
                        : message.getHeader().isSetField(50)
                        ? message.getHeader().getString(50)
                        : null;

                System.out.println(clientId);
                System.out.println(clientIds);
                if (clientIds == null || (clientId != null && clientIds.contains(clientId))) {
                    System.out.println(message);
                    message = normalizationService.applyNormalizationRules(ruleService.getAllRules(), message);
                    System.out.println(message);
                    System.out.println("-----Normalized-----");
                }
            }

            System.out.println(route.getSenderCompID() + " " + route.getTargetCompID());
            final boolean result = Session.sendToTarget( message, route.getSenderCompID(), route.getTargetCompID() );

            if(!result){

//                final String server = FixTargetSession.from( senderCompID ).get().getSender().name();
                final String server = route.getSenderCompID();
//                final String refSeqNum = AppUtils.getString( message.getHeader(), MsgSeqNum.FIELD );
//                final String refMsgType = AppUtils.getString( message.getHeader(), MsgType.FIELD);
//
//                message.getHeader().removeField( OnBehalfOfCompID.FIELD );
//                message.getHeader().setField( new MsgType("3") );
//
//                message.setField( new ExecType( ExecType.REJECTED ) );
//                message.setField( new OrdStatus( OrdStatus.REJECTED ));
//                message.setField( new TransactTime( LocalDateTime.now() ) );
//                message.setField( new Text( StringUtils.replace( route.getTargetCompID() + " session is currently down", " ", "_" )  ) );
//                message.setString( RefSeqNum.FIELD, refSeqNum );
//                message.setString( RefMsgType.FIELD, refMsgType );
//                message.setInt(SessionRejectReason.FIELD, SessionRejectReason.DECRYPTION_PROBLEM);

//                message.removeField(ClOrdID.FIELD);
//                message.removeField(HandlInst.FIELD);
//                message.removeField(OrderQty.FIELD);
//                message.removeField(OrdStatus.FIELD);
//                message.removeField(OrdType.FIELD);
//                message.removeField(Side.FIELD);
//                message.removeField(Symbol.FIELD);
//                message.removeField(TimeInForce.FIELD);
//                message.removeField(TransactTime.FIELD);



                Message executionReport = messageFactory.create(message.getHeader().getString(BeginString.FIELD), MsgType.EXECUTION_REPORT);
                executionReport.getHeader().setField(new SenderCompID(message.getHeader().getString(49)));
                executionReport.getHeader().setField(new TargetCompID(message.getHeader().getString(56)));
                executionReport.getHeader().setField(new SendingTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime()));
                executionReport.getHeader().setField(new MsgSeqNum(message.getHeader().getInt(34) + 1));

                executionReport.setField(new ClOrdID(message.getString(11)));
                executionReport.setField(new ExecID("E" + message.getString(11)));
                executionReport.setField(new OrdStatus(OrdStatus.REJECTED));
                executionReport.setField(new Text(StringUtils.replace( route.getTargetCompID() + " session is currently down", " ", "_" )));
                executionReport.setField(new LastShares());
                executionReport.setField(new Side(message.getChar(54)));
                executionReport.setField(new Symbol(message.getString(55)));
                executionReport.setField(new ExecType(ExecType.REJECTED));
                executionReport.setField(new OrderID("O" + message.getString(11)));
                if(!message.getHeader().getString(BeginString.FIELD).contains("4.4"))
                    executionReport.setField(new ExecTransType(ExecTransType.NEW));
                executionReport.setField(new OrderQty(message.getInt(38)));
                executionReport.setField(new LeavesQty(message.getInt(38)));
                executionReport.setField(new AvgPx());
                executionReport.setField(new CumQty());

                Session.sendToTarget( executionReport, server, senderCompID );
            }

        } catch (Exception ex) {
            ex.printStackTrace();;
        }

    }
}
