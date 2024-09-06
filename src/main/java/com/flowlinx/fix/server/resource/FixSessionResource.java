package com.flowlinx.fix.server.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowlinx.fix.server.DB;
import com.flowlinx.fix.server.domain.Rule;
import com.flowlinx.fix.server.fix.FixSessionBuilder;
import com.flowlinx.fix.server.representation.FixSessionIdRepresentation;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import com.flowlinx.fix.server.utils.AppConstants;
import com.flowlinx.fix.server.utils.FixConstants;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import quickfix.*;
import quickfix.mina.SessionConnector;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//@Secured(AppConstants.ROLE_ADMIN)
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/fix/session")
public class FixSessionResource {

    @Autowired
    private Mapper mapper;

    @Autowired
    private ThreadedSocketAcceptor acceptor;

    @Autowired(required = false)
    private ThreadedSocketInitiator initiator;

    @Autowired
    private FixSessionBuilder builder;

    @GetMapping
    public ResponseEntity<List<FixSessionIdRepresentation>> sessions() {

        final List<FixSessionIdRepresentation> result = new ArrayList<>();

        acceptor.getSessions().stream().forEach( s -> {
            final FixSessionIdRepresentation session = mapper.map( s, FixSessionIdRepresentation.class );
            session.setConnectionType("Acceptor");
            result.add( session );

        });

        initiator.getSessions().stream().forEach( s -> {
            final FixSessionIdRepresentation session = mapper.map( s, FixSessionIdRepresentation.class );
            session.setConnectionType("Initiator");
            result.add( session );
        });

        return ResponseEntity.ok( result );
    }

    @GetMapping( value = {"/{connectionType}/{sessionID}"})
    public ResponseEntity<FixSessionRepresentation> getSession(
            @PathVariable(value = "connectionType") String connectionType,
            @PathVariable(value = "sessionID") String id ) throws IOException, ConfigError {

        final SessionConnector connector = FixConstants.CONNECTION_TYPE_ACCEPTOR.equalsIgnoreCase( connectionType )
                ? acceptor : initiator;

        final List<SessionID> sessions = connector.getSessions();

        for( SessionID sessionID : sessions ){
            if( sessionID.getTargetCompID().endsWith( id ) ){
                final Session session = Session.lookupSession( sessionID );

                final Properties properties = connector.getSettings().getSessionProperties( sessionID );

                return ResponseEntity.ok( builder.build( sessionID, session, properties ) );
            }
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping( value = {"/{sessionID}/sender/seqnum/{seqNum}"})
    public ResponseEntity<Void> resetSenderSeqNum(
            @PathVariable(value = "sessionID") String id,
            @PathVariable(value = "seqNum") Integer seqNum ) {

        ResponseEntity result = ResponseEntity.notFound().build();

        try {
            final List<SessionID> sessionList = acceptor.getSessions();

            for( SessionID sessionID : sessionList ){

                if( sessionID.getTargetCompID().endsWith( id ) ){
                    final Session session = Session.lookupSession( sessionID );
                    session.setNextSenderMsgSeqNum( seqNum );
                    result = ResponseEntity.ok("Sequence number set was to " + seqNum);
                }
            }


            final List<SessionID> sessionListInitiator = initiator.getSessions();

            for( SessionID sessionID : sessionListInitiator ){
                System.out.println(sessionID.getTargetCompID() + " " + id);
                if( sessionID.getTargetCompID().endsWith( id ) ){
                    System.out.println("Sequence number was set to " + seqNum);
                    final Session session = Session.lookupSession( sessionID );
                    session.setNextSenderMsgSeqNum( seqNum );
                    result = ResponseEntity.ok("Sequence number was set to " + seqNum);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping( value = {"/{sessionID}/target/seqnum/{seqNum}"})
    public ResponseEntity<String> resetTargetSeqNum(
            @PathVariable(value = "sessionID") String id,
            @PathVariable(value = "seqNum") Integer seqNum ) {

        ResponseEntity result = ResponseEntity.notFound().build();

        try {
            final List<SessionID> sessionList = acceptor.getSessions();

            for( SessionID sessionID : sessionList ){

                if( sessionID.getTargetCompID().endsWith( id ) ){
                    final Session session = Session.lookupSession( sessionID );
                    session.setNextTargetMsgSeqNum( seqNum );
                    result = ResponseEntity.ok("Sequence number was set to " + seqNum);
                }
            }


            final List<SessionID> sessionListInitiator = initiator.getSessions();

            for( SessionID sessionID : sessionListInitiator ){
                System.out.println(sessionID.getTargetCompID() + " " + id);
                if( sessionID.getTargetCompID().endsWith( id ) ){
                    System.out.println("Sequence number was set to " + seqNum);
                    final Session session = Session.lookupSession( sessionID );
                    session.setNextTargetMsgSeqNum( seqNum );
                    result = ResponseEntity.ok("Sequence number was set to " + seqNum);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    @GetMapping( value = {"/rules"})
    public ResponseEntity<Rule> getRules() {
        System.out.println("GET RULES....");
        ObjectMapper objectMapper = new ObjectMapper();
        Rule rule = null;
//        try {
//            File file = new File("/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/newRules.json");
//            rule = objectMapper.readValue(file, Rule.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
        return ResponseEntity.ok(rule);
    }


    @GetMapping( value = {"/resetSessionConfigFile"})
    public ResponseEntity<Void> resetSessionConfigFile() {
        System.out.println("resetSessionConfigFile....");
        ResponseEntity result = ResponseEntity.notFound().build();
        try {
            DB.createAllFilesFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result = ResponseEntity.ok("Session Config file has been reset");
        return result;
    }


//    @GetMapping( value = {"/{sessionID}/sender/seqnum/{seqNum}"})
//    public ResponseEntity<Void> getSenderSeqNum(@PathVariable(value = "beginString") String beginString,
//            @PathVariable(value = "sessionID") String id) {
//
//        ResponseEntity result = ResponseEntity.notFound().build();
//
//        try {
//            final List<SessionID> sessionList = acceptor.getSessions();
//
//            for( SessionID sessionID : sessionList ){
//
//                if( sessionID.getTargetCompID().endsWith( id ) ){
//                    final Session session = Session.lookupSession( sessionID );
//                    session.setNextSenderMsgSeqNum( seqNum );
//                    result = ResponseEntity.ok("Sequence number set was to " + seqNum);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }




}