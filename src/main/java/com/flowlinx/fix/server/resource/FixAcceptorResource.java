package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.resource.representation.FixSessionIdRepresentation;
import com.flowlinx.fix.server.resource.representation.FixSessionRepresentation;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import quickfix.Acceptor;
import quickfix.Session;
import quickfix.SessionID;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = "/fix/acceptor")
public class FixAcceptorResource {

    @Autowired
    private Mapper mapper;

    //@Autowired
    private Acceptor acceptor;

    @GetMapping( value = {"/session"})
    public ResponseEntity<List<SessionID>> sessions() {
        return ResponseEntity.ok( acceptor.getSessions() );
    }

    @GetMapping( value = {"/session/{sessionID}"})
    public ResponseEntity<FixSessionRepresentation> getSession(
            @PathVariable(value = "sessionID") String id ) {

        try {
            final List<SessionID> sessionList = acceptor.getSessions();

            for( SessionID sessionID : sessionList ){

                if( sessionID.getTargetCompID().endsWith( id ) ){
                    final Session session = Session.lookupSession( sessionID );

                    final FixSessionIdRepresentation sessionIdRepresentation = FixSessionIdRepresentation.builder()
                            .id( sessionID.toString() )
                            .beginString( sessionID.getBeginString() )
                            .targetCompID( sessionID.getTargetCompID() )
                            .targetLocationID( sessionID.getTargetLocationID() )
                            .targetSubID( sessionID.getTargetSubID() )
                            .senderCompID( sessionID.getSenderCompID() )
                            .senderLocationID( sessionID.getSenderLocationID() )
                            .senderSubID( sessionID.getSenderSubID() )
                            .sessionQualifier( sessionID.getSessionQualifier() )
                            .build();

                    final FixSessionRepresentation representation = mapper.map( session, FixSessionRepresentation.class);
                    representation.setSessionID( sessionIdRepresentation );
                    representation.setDictionaryVersion( session.getDataDictionary().getVersion() );
                    representation.setExpectedSenderNum( session.getExpectedSenderNum() );
                    representation.setExpectedTargetNum( session.getExpectedTargetNum() );
                    representation.setStartTime( session.getStartTime() );
                    representation.setResponderRemoteAddress( session.getResponder().getRemoteAddress() );
                    representation.setRemoteAddress( session.getRemoteAddress() );
                    representation.setLogonTimeout( session.getLogonTimeout() );
                    representation.setLogoutTimeout( session.getLogoutTimeout() );

                    return ResponseEntity.ok( representation );
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping( value = {"/session/{sessionID}/sender/seqnum/{seqNum}"})
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

    @GetMapping( value = {"/session/{sessionID}/target/seqnum/{seqNum}"})
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