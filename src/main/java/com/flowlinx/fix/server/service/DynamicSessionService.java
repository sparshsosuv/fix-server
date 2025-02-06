package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.DynamicSession;
import com.flowlinx.fix.server.domain.FixSessionExt;
import com.flowlinx.fix.server.repository.DynamicSessionRepository;
import com.flowlinx.fix.server.repository.FixSessionExtRepository;
import com.flowlinx.fix.server.resource.representation.CreateSessionRepresentation;
import com.flowlinx.fix.server.resource.representation.DeleteSessionRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.health.ConnectorType;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.*;
import quickfix.field.BeginString;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static quickfix.SessionSettings.*;

@Slf4j
@Service
@Transactional
public class DynamicSessionService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private DynamicSessionRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private FixSessionExtRepository fixSessionExtRepository;

    @Autowired(required = false)
    private ThreadedSocketAcceptor socketAcceptor;

    @Autowired(required = false)
    private ThreadedSocketInitiator socketInitiator;

//    @Autowired(required = false)
//    private ThreadedSocketAcceptor socketAcceptor;
//
//    @Autowired(required = false)
//    private ThreadedSocketInitiator socketInitiator;

    @Autowired
    private SessionSettings serverSessionSettings;

    @Autowired
    private SessionSettings clientSessionSettings;


    public void create( CreateSessionRepresentation item ) {
        final DynamicSession session = mapper.map( item, DynamicSession.class );

//        repository.save( session );

        addDynamicSessions( Arrays.asList( item ), true );

        SessionID sessionID = new SessionID(new BeginString(item.getBeginString()),
                new SenderCompID(item.getSenderCompID()),
                new TargetCompID(item.getTargetCompID()));

        try {
            if (item.getConnectionType().toLowerCase().equals("acceptor")) {
                socketAcceptor.start();
            }
            else if (item.getConnectionType().toLowerCase().equals("initiator"))
                socketInitiator.start();
        } catch ( Exception e ) {
            e.printStackTrace();
            log.error( String.format("Error on creating fix session (socketAcceptor/socketInitiator): %s", e.getMessage()));
        }

    }

    public void update( CreateSessionRepresentation item ) {
        final DynamicSession session = mapper.map( item, DynamicSession.class );

        DeleteSessionRepresentation deleteItem = new DeleteSessionRepresentation();

        String[] parts = item.getId().split(":", 2);
        String beginString = parts[0];
        String rest = parts[1];

        int lastDotIndex = rest.lastIndexOf('.');

        String senderCompId = rest.substring(0, lastDotIndex);
        String targetCompId = rest.substring(lastDotIndex + 1);

        deleteItem.setBeginString(beginString);
        deleteItem.setSenderCompID(senderCompId);
        deleteItem.setTargetCompID(targetCompId);

        delete(deleteItem);
//        repository.save( session );

        addDynamicSessions( Arrays.asList( item ), true );
    }

    public void delete( DeleteSessionRepresentation item ) {
        log.info("Deleting session: " + item);
        log.info("Details: " + item.getBeginString() + " " + item.getConnectionType() + " " + item.getTargetCompID());

        final DynamicSession session = mapper.map( item, DynamicSession.class );

//        repository.delete( session );

        removeDynamicSessions(item);
//        addDynamicSessions( Arrays.asList( item ), true );

    }

    public List<CreateSessionRepresentation> getAllDynamicSessions() {
        List<DynamicSession> dynamicSessions = repository.findAll();

        return dynamicSessions.stream().map(session -> mapper.map(
                session, CreateSessionRepresentation.class)).collect(Collectors.toList());
    }

    public void removeDynamicSessions(DeleteSessionRepresentation item) {
        SessionID sessionID = new SessionID(
                new BeginString(item.getBeginString()),
                new SenderCompID(item.getSenderCompID()),
                new TargetCompID(item.getTargetCompID())
        );

        String connectionType = (item.getConnectionType() != null) ? item.getConnectionType().trim().toLowerCase() : "";

        if (connectionType.isEmpty() || connectionType.equals("acceptor")) {
            try {
                socketAcceptor.removeDynamicSession(sessionID);
//                serverSessionSettings.removeSetting(sessionID, "ConnectionType");
//                serverSessionSettings.removeSetting(sessionID, "StartTime");
//                serverSessionSettings.removeSetting(sessionID, "EndTime");
//                serverSessionSettings.removeSetting(sessionID, "HeartBtInt");
//                serverSessionSettings.removeSetting(sessionID, "ValidOrderTypes");
//                serverSessionSettings.removeSetting(sessionID, "UseDataDictionary");
//                serverSessionSettings.removeSetting(sessionID, "DefaultMarketPrice");
//                serverSessionSettings.removeSetting(sessionID, "ValidateUserDefinedFields");
//                serverSessionSettings.removeSetting(sessionID, "PersistMessages");
//                serverSessionSettings.removeSetting(sessionID, "BeginString");
//                serverSessionSettings.removeSetting(sessionID, "SocketAcceptPort");
//                System.out.println("serverSessionSettings.getSessionProperties(sessionID): " + serverSessionSettings.getSessionProperties(sessionID));
//                serverSessionSettings.getSessionProperties(sessionID).remove(sessionID);
//                for (Map.Entry<Object, Object> entry : serverSessionSettings.getSessionProperties(sessionID)) {
//                    Object key = entry.getKey();
//                    Object value = entry.getValue();
//
//                    // Process key and value here
//                    System.out.println("Key: " + key + ", Value: " + value);
//                    serverSessionSettings.getSessionProperties(sessionID, key);
//                }

                socketAcceptor.start();
            } catch (Exception e) {
                // Log the exception or handle it appropriately
                e.printStackTrace();
                log.error( String.format( "Error on removing fix session (socketAcceptor/socketInitiator): %s", e.getMessage()));
            }
        }

        if (connectionType.isEmpty() || !connectionType.equals("acceptor")) {
            try {
                socketInitiator.removeDynamicSession(sessionID);
                socketInitiator.start();
            } catch (Exception e) {
                // Log the exception or handle it appropriately
                e.printStackTrace();
                log.error( String.format( "Error on removing fix session (socketAcceptor/socketInitiator): %s", e.getMessage()));

            }
        }
    }

    public void addDynamicSessions(List<CreateSessionRepresentation> sessionsRepresentation, boolean restartSocketAcceptor)  {

        try {
            for (CreateSessionRepresentation sessionRepresentation : sessionsRepresentation) {
                SessionID sessionID = new SessionID(new BeginString(sessionRepresentation.getBeginString()),
                        new SenderCompID(sessionRepresentation.getSenderCompID()),
                        new TargetCompID(sessionRepresentation.getTargetCompID()));

                Dictionary dictionary = new Dictionary();
                dictionary.setString("ConnectionType", sessionRepresentation.getConnectionType().toLowerCase());
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
                if (sessionRepresentation.getConnectionType().toLowerCase().equals("acceptor"))
                    dictionary.setString("SocketAcceptPort", sessionRepresentation.getSocketAcceptPort());
                if (sessionRepresentation.getConnectionType().toLowerCase().equals("initiator")) {
                    dictionary.setString("SocketConnectPort", sessionRepresentation.getSocketConnectPort());
                    dictionary.setString("SocketConnectHost", sessionRepresentation.getSocketConnectHost());
                }
                dictionary.setString("SenderCompID", sessionRepresentation.getSenderCompID());
                dictionary.setString("TargetCompID", sessionRepresentation.getTargetCompID());
                dictionary.setString("ResetOnDisconnect", sessionRepresentation.getResetOnDisconnect());
                dictionary.setString("ResetOnLogout", sessionRepresentation.getResetOnLogout());

                if (sessionRepresentation.getConnectionType().toLowerCase().equals("acceptor"))
                    socketAcceptor.getSettings().set(sessionID, dictionary);
                else
                    socketInitiator.getSettings().set(sessionID, dictionary);

                final SessionSettings settings = new SessionSettings();
                if (sessionRepresentation.getConnectionType().toLowerCase().equals("acceptor"))
                    copySettings(settings, socketAcceptor.getSettings().getDefaultProperties());
                else
                    copySettings(settings, socketInitiator.getSettings().getDefaultProperties());

                settings.setString(BEGINSTRING, sessionRepresentation.getBeginString());
                settings.setString(SENDERCOMPID, sessionRepresentation.getSenderCompID());
                settings.setString(TARGETCOMPID, sessionRepresentation.getTargetCompID());
                if (sessionRepresentation.getConnectionType().toLowerCase().equals("initiator"))
                    settings.setString("ConnectionType", sessionRepresentation.getConnectionType().toLowerCase());

                final Session session = sessionFactory.create(sessionID, settings);
                if (sessionRepresentation.getConnectionType().toLowerCase().equals("acceptor"))
                    socketAcceptor.addDynamicSession( session );
                else
                    socketInitiator.addDynamicSession( session );
//                socketAcceptor.start();
            }

            log.info("Dynamic sessions added successfully");

//        try {
//            final List<SessionID> acceptorSessions = socketAcceptor.getSessions();
//            final List<SessionID> initiatorSessions = socketInitiator.getSessions();
//
//            for( SessionID sessionID : acceptorSessions ){
//
//                final Properties properties = socketAcceptor.getSettings().getSessionProperties( sessionID );
//
//                String sId = properties.getProperty("SenderCompID") + "." + properties.getProperty("TargetCompID");
//                String fixVersion = properties.getProperty("BeginString");
//                FixSessionExt fixSessionExt = fixSessionExtRepository.findBySessionIdAndFixVersion(sId, fixVersion);
//
//                if (fixSessionExt == null) {
//                    fixSessionExt = new FixSessionExt();
//                }
//
//                fixSessionExt.setSessionId(sId);
//                fixSessionExt.setSenderCompId(properties.getProperty("SenderCompID"));
//                fixSessionExt.setTargetCompId(properties.getProperty("TargetCompID"));
//                fixSessionExt.setConnectionType("");
////                if (fixSessionExt.getSessionStatus().equals(("UP")))
////                    fixSessionExt.setSessionStatus("UP");
//                fixSessionExt.setConnectionType("Acceptor");
//                fixSessionExt.setFirmName("");
//                fixSessionExt.setStartTime("");
//                fixSessionExt.setEndTime("");
//                fixSessionExt.setIpAddress("");
//                if (!Long.valueOf(properties.getProperty("SocketAcceptPort")).equals(fixSessionExt.getPort()))
//                    fixSessionExt.setPort(Long.valueOf(properties.getProperty("SocketAcceptPort")));
//                fixSessionExt.setFixUser("");
//                fixSessionExt.setFixPassword("");
//                fixSessionExt.setHeartbeatInterval(0L);
//                fixSessionExt.setFixVersion(fixVersion);
//                fixSessionExt.setSenderSubId("");
//                fixSessionExt.setSenderLocationId("");
//                fixSessionExt.setTargetSubId("");
//                fixSessionExt.setTargetLocationId("");
//                fixSessionExt.setApplicationType("");
//                fixSessionExt.setDataDictionary("");
//                fixSessionExt.setCounterpartyType("");
//                fixSessionExt.setRoutingTag("");
//                fixSessionExt.setTimestamp(Timestamp.from(Instant.now()));
//                fixSessionExt.setUseDataDictionary("");
//                fixSessionExt.setInstance("");
//                fixSessionExt.setRegion("");
//                fixSessionExt.setHub("");
//                fixSessionExt.setTimeZone("");
//                fixSessionExt.setDisplayStartTime("");
//                fixSessionExt.setDisplayEndTime("");
//                fixSessionExt.setNormalization(false);
//                fixSessionExt.setLastUpdated(Timestamp.from(Instant.now()));
//                fixSessionExtRepository.save(fixSessionExt);
//                System.out.println("SESSION::::::: "+ fixSessionExt.getSessionId() + " " + fixSessionExt.getFixVersion() + " " + fixSessionExt.getSessionStatus());
//            }
//
//            for( SessionID sessionID : initiatorSessions ){
//                final Properties properties = socketInitiator.getSettings().getSessionProperties( sessionID );
//
//                String sId = properties.getProperty("SenderCompID") + "." + properties.getProperty("TargetCompID");
//                String fixVersion = properties.getProperty("BeginString");
//                FixSessionExt fixSessionExt = fixSessionExtRepository.findBySessionIdAndFixVersion(sId, fixVersion);
//
//                if (fixSessionExt == null) {
//                    fixSessionExt = new FixSessionExt();
//                }
//
//                fixSessionExt.setSessionId(sId);
//                fixSessionExt.setSenderCompId(properties.getProperty("SenderCompID"));
//                fixSessionExt.setTargetCompId(properties.getProperty("TargetCompID"));
//                fixSessionExt.setConnectionType("");
////                fixSessionExt.setSessionStatus("DOWN");
//                fixSessionExt.setConnectionType("Initiator");
//                fixSessionExt.setFirmName("");
//                fixSessionExt.setStartTime("");
//                fixSessionExt.setEndTime("");
//                fixSessionExt.setIpAddress(properties.getProperty("SocketConnectHost"));
//                if (!Long.valueOf(properties.getProperty("SocketConnectPort")).equals(fixSessionExt.getPort()))
//                    fixSessionExt.setPort(Long.valueOf(properties.getProperty("SocketConnectPort")));
//                fixSessionExt.setFixUser("");
//                fixSessionExt.setFixPassword("");
//                fixSessionExt.setHeartbeatInterval(0L);
//                fixSessionExt.setFixVersion(fixVersion);
//                fixSessionExt.setSenderSubId("");
//                fixSessionExt.setSenderLocationId("");
//                fixSessionExt.setTargetSubId("");
//                fixSessionExt.setTargetLocationId("");
//                fixSessionExt.setApplicationType("");
//                fixSessionExt.setDataDictionary("");
//                fixSessionExt.setCounterpartyType("");
//                fixSessionExt.setRoutingTag("");
//                fixSessionExt.setTimestamp(Timestamp.from(Instant.now()));
//                fixSessionExt.setUseDataDictionary("");
//                fixSessionExt.setInstance("");
//                fixSessionExt.setRegion("");
//                fixSessionExt.setHub("");
//                fixSessionExt.setTimeZone("");
//                fixSessionExt.setDisplayStartTime("");
//                fixSessionExt.setDisplayEndTime("");
//                fixSessionExt.setNormalization(false);
//                fixSessionExt.setLastUpdated(Timestamp.from(Instant.now()));
//                fixSessionExtRepository.save(fixSessionExt);
//                System.out.println("SESSION::::::: "+ fixSessionExt.getSessionId() + " " + fixSessionExt.getFixVersion() + " " + fixSessionExt.getSessionStatus());
//            }
//
//        }  catch (ConfigError e) {
//            throw new RuntimeException(e);
//        }
//
        } catch ( Exception e ) {
            e.printStackTrace();
            log.error( String.format( "Error on creating fix session: %s", e.getMessage()));
        }
    }

    protected void copySettings(SessionSettings settings, Properties properties) {
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            settings.setString((String) e.getKey(), e.getValue().toString());
        }
    }
}
