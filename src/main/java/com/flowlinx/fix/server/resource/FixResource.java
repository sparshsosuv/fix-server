package com.flowlinx.fix.server.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import quickfix.Session;
import quickfix.SessionID;

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = "/fix")
public class FixResource {

    @GetMapping( value = {"/session/{sessionID}/sender/seqnum/{seqNum}"})
    public ResponseEntity<String> resetSenderSeqNum(
            @PathVariable(value = "sessionID") String sessionID,
            @PathVariable(value = "seqNum") Integer seqNum ) {

        String result = "OK";
        try {
            final Session session = Session.lookupSession( new SessionID( sessionID ) );
            session.setNextSenderMsgSeqNum( seqNum );
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return ResponseEntity.ok( result );
    }

    @GetMapping( value = {"/session/{sessionID}/target/seqnum/{seqNum}"})
    public ResponseEntity<String> resetTargetSeqNum(
            @PathVariable(value = "sessionID") String sessionID,
            @PathVariable(value = "seqNum") Integer seqNum ) {

        String result = "OK";
        try {
            final Session session = Session.lookupSession( new SessionID( sessionID ) );
            session.setNextTargetMsgSeqNum( seqNum );
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return ResponseEntity.ok( result );
    }


}