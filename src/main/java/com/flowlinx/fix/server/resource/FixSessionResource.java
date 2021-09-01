package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.FixSessionBuilder;
import com.flowlinx.fix.server.representation.FixSessionIdRepresentation;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import com.flowlinx.fix.server.utils.FixConstants;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quickfix.Acceptor;
import quickfix.Initiator;
import quickfix.Session;
import quickfix.SessionID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/fix/session")
public class FixSessionResource {

    @Autowired
    private Mapper mapper;

    @Autowired
    private Acceptor acceptor;

    @Autowired
    private Initiator initiator;

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
            @PathVariable(value = "sessionID") String id ) throws IOException {

        final List<SessionID> sessions = FixConstants.CONNECTION_TYPE_ACCEPTOR.equalsIgnoreCase( connectionType )
                ? acceptor.getSessions() : initiator.getSessions();

        for( SessionID sessionID : sessions ){
            if( sessionID.getTargetCompID().endsWith( id ) ){
                final Session session = Session.lookupSession( sessionID );
                return ResponseEntity.ok( builder.build( sessionID, session ) );
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}