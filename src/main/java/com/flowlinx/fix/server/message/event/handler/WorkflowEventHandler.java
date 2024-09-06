package com.flowlinx.fix.server.message.event.handler;

import com.flowlinx.fix.server.service.RuleService;
import com.flowlinx.fix.server.utils.FixConstants;
import com.flowlinx.fix.server.message.event.WorkflowEvent;
import com.flowlinx.fix.server.type.FixSenderSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;

import java.sql.Timestamp;

@Slf4j
@Component
public class WorkflowEventHandler implements ApplicationListener<WorkflowEvent> {

    private static final String FLX_WORKFLOW = "SOSUV_WORKFLOW";

    private DefaultMessageFactory messageFactory = new DefaultMessageFactory();

    @Autowired
    private RuleService ruleService;

    @Autowired(required = false)
    private ThreadedSocketAcceptor acceptor;

    @Autowired(required = false)
    private ThreadedSocketInitiator initiator;

    @Override
    public void onApplicationEvent(WorkflowEvent event) {

        try{
            final Message message = event.getMessage();
            final String senderCompId = message.getHeader().getString( SenderCompID.FIELD );
            message.setString( FixConstants.FLX_TARGET_COMP_ID, senderCompId );

            boolean result;
            final String fixVersion = message.getHeader().getField(new BeginString()).getValue();
            final String server = fixVersion.equals("FIX.4.4") ? FixSenderSession.SOSUV_SERVER_44.name() : FixSenderSession.SOSUV_SERVER.name();

            Session session = Session.lookupSession(new SessionID(
                    message.getHeader().getString(BeginString.FIELD),
                                server,
                                FLX_WORKFLOW
            ));
            System.out.println(session.isEnabled());
            System.out.println(session.isLoggedOn());

            if (session == null || !session.isEnabled() || !session.isLoggedOn()) {
                System.out.println("Bad sesssion...");
            }

                result = Session.sendToTarget( message, server, FLX_WORKFLOW );

            if(!result){

//                final String server = FixTargetSession.from( senderCompID ).get().getSender().name();
//                final String refSeqNum = AppUtils.getString( message.getHeader(), MsgSeqNum.FIELD );
//                final String refMsgType = AppUtils.getString( message.getHeader(), MsgType.FIELD);
//
//                message.getHeader().removeField( OnBehalfOfCompID.FIELD );
//                message.getHeader().setField( new MsgType("3") );
//
//                message.setField( new ExecType( ExecType.REJECTED ) );
//                message.setField( new OrdStatus( OrdStatus.REJECTED ));
//                message.setField( new TransactTime( LocalDateTime.now() ) );
//                message.setField( new Text( StringUtils.replace( FLX_WORKFLOW + " session is currently down", " ", "_" )  ) );
//                message.setString( RefSeqNum.FIELD, refSeqNum );
//                message.setString( RefMsgType.FIELD, refMsgType );
//                message.setInt(SessionRejectReason.FIELD, SessionRejectReason.DECRYPTION_PROBLEM);

                Message executionReport = messageFactory.create(message.getHeader().getString(BeginString.FIELD), MsgType.EXECUTION_REPORT);
                executionReport.getHeader().setField(new SenderCompID(message.getHeader().getString(49)));
                executionReport.getHeader().setField(new TargetCompID(message.getHeader().getString(56)));
                executionReport.getHeader().setField(new SendingTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime()));
                executionReport.getHeader().setField(new MsgSeqNum(message.getHeader().getInt(34) + 1));

                executionReport.setField(new ClOrdID(message.getString(11)));
                executionReport.setField(new ExecID("E" + message.getString(11)));
                executionReport.setField(new OrdStatus(OrdStatus.REJECTED));
                executionReport.setField(new Text(StringUtils.replace( FLX_WORKFLOW + "-" + message.getHeader().getString(BeginString.FIELD) + " session is currently down", " ", "_" )));
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


                Session.sendToTarget( executionReport, server, senderCompId );
            }
        } catch (SessionNotFound | FieldNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            log.error("Session not found");
        }
    }

}
