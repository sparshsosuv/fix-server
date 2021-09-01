package com.flowlinx.fix.server;

import com.flowlinx.fix.server.representation.FixSessionIdRepresentation;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickfix.Session;
import quickfix.SessionID;

import java.io.IOException;

@Service
public class FixSessionBuilder {

    @Autowired
    private Mapper mapper;

    public FixSessionRepresentation build(SessionID sessionID, Session session) throws IOException {

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
        representation.setResponderRemoteAddress( session.getResponder() != null ? session.getResponder().getRemoteAddress() : "Disconnected" );
        representation.setRemoteAddress( session.getRemoteAddress() );
        representation.setLogonTimeout( session.getLogonTimeout() );
        representation.setLogoutTimeout( session.getLogoutTimeout() );

        return representation;
    }
}
