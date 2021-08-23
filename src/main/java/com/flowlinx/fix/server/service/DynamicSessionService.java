package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.DynamicSession;
import com.flowlinx.fix.server.repository.DynamicSessionRepository;
import com.flowlinx.fix.server.resource.representation.CreateSessionRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.*;
import quickfix.field.BeginString;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DynamicSessionService {

    @Autowired
    private ThreadedSocketAcceptor socketAcceptor;

    @Autowired
    private Mapper mapper;

    @Autowired
    private DynamicSessionRepository repository;

    public void create( CreateSessionRepresentation sessionRepresentation ) {
        DynamicSession session = mapper.map( sessionRepresentation, DynamicSession.class );

        repository.save( session );

        addDynamicSessions( Arrays.asList( sessionRepresentation ), true );

    }

    public List<CreateSessionRepresentation> getAllDynamicSessions() {
        List<DynamicSession> dynamicSessions = repository.findAll();

        return dynamicSessions.stream().map(session -> mapper.map(
                session, CreateSessionRepresentation.class)).collect(Collectors.toList());
    }

    public void addDynamicSessions(List<CreateSessionRepresentation> sessionsRepresentation, boolean restartSocketAcceptor)  {

        try {
            for (CreateSessionRepresentation sessionRepresentation : sessionsRepresentation) {
                SessionID sessionID = new SessionID(new BeginString(sessionRepresentation.getBeginString()),
                        new SenderCompID(sessionRepresentation.getSenderCompID()),
                        new TargetCompID(sessionRepresentation.getTargetCompID()),
                        "Session " + System.currentTimeMillis() );

                Dictionary dictionary = new Dictionary();

                dictionary.setString("ConnectionType", sessionRepresentation.getConnectionType());
                dictionary.setString("StartTime", sessionRepresentation.getStartTime());
                dictionary.setString("EndTime", sessionRepresentation.getEndTime());
                dictionary.setString("HeartBtInt", sessionRepresentation.getHeartBtInt());
                dictionary.setString("ValidOrderTypes", sessionRepresentation.getValidOrderTypes());
                dictionary.setString("UseDataDictionary", sessionRepresentation.getUseDataDictionary());
                dictionary.setString("DefaultMarketPrice", sessionRepresentation.getDefaultMarketPrice());
                dictionary.setString("ValidateUserDefinedFields", sessionRepresentation.getValidateUserDefinedFields());
                dictionary.setString("ValidateFieldsOutOfOrder", sessionRepresentation.getValidateFieldsOutOfOrder());
                dictionary.setString("ValidateFieldsHaveValue", sessionRepresentation.getValidateFieldsHaveValue());
                dictionary.setString("PersistMessages", sessionRepresentation.getPersistMessages());
                dictionary.setString("BeginString", sessionRepresentation.getBeginString());
                dictionary.setString("SocketAcceptPort", sessionRepresentation.getSocketAcceptPort());
                dictionary.setString("SenderCompID", sessionRepresentation.getSenderCompID());
                dictionary.setString("TargetCompID", sessionRepresentation.getTargetCompID());
                dictionary.setString("ResetOnDisconnect", sessionRepresentation.getResetOnDisconnect());
                dictionary.setString("ResetOnLogout", sessionRepresentation.getResetOnLogout());
                dictionary.setString("DataDictionary", sessionRepresentation.getDataDictionary());

                //dictionary.setString("FileLogPath", "Log");
                //dictionary.setString("FileStorePath", "c:\fixfiles");

                socketAcceptor.getSettings().set(sessionID, dictionary);

            }

            if( restartSocketAcceptor ) {
                socketAcceptor.stop();
                socketAcceptor.start();
            }

            log.info("Dynamic sessions added successfully");

        } catch ( Exception e ) {
            log.error( String.format( "Error on creating fix session: %s", e.getMessage()));
        }
    }
}
