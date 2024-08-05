package com.flowlinx.fix.server.fix;

import com.flowlinx.fix.server.representation.FixSessionIdRepresentation;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import org.apache.commons.lang3.math.NumberUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickfix.Acceptor;
import quickfix.Initiator;
import quickfix.Session;
import quickfix.SessionID;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Service
public class FixSessionBuilder {

    @Autowired
    private Mapper mapper;

    public FixSessionRepresentation build(SessionID sessionID, Session session, Properties prop) throws IOException {

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

        final FixSessionRepresentation item = mapper.map( session, FixSessionRepresentation.class);
        item.setSessionID( sessionIdRepresentation );
        item.setDictionaryVersion( session.getDataDictionary().getVersion() );
        item.setExpectedSenderNum( session.getExpectedSenderNum() );
        item.setExpectedTargetNum( session.getExpectedTargetNum() );
        item.setStartTime( session.getStartTime() );
        item.setRemoteAddress( session.getRemoteAddress() );
        item.setLogonTimeout( session.getLogonTimeout() );
        item.setLogoutTimeout( session.getLogoutTimeout() );
        item.setValidateFieldsOutOfOrder( getStrValue( prop, Session.SETTING_VALIDATE_FIELDS_OUT_OF_ORDER ) );
        item.setValidateFieldsHaveValue( getStrValue( prop, Session.SETTING_VALIDATE_FIELDS_HAVE_VALUES ) );
        item.setValidateUserDefinedFields( getStrValue( prop,  Session.SETTING_VALIDATE_USER_DEFINED_FIELDS ) );
        item.setSocketAcceptPort( getIntValue( prop,  Acceptor.SETTING_SOCKET_ACCEPT_PORT ) );
        item.setSocketConnectHost( getStrValue( prop, Initiator.SETTING_SOCKET_CONNECT_HOST ) );
        item.setSocketAcceptPort( getIntValue( prop,  Initiator.SETTING_SOCKET_CONNECT_PORT ) );
        item.setNormalization(prop.getProperty("Normalization").equals("Y"));
        item.setNormalizationClientIds(prop.getProperty("NormalizationClientIds"));

        return item;
    }

    private String getStrValue(Properties prop, String key){
        final Optional objKey = prop.keySet().stream()
                .filter(k -> k.toString().equalsIgnoreCase( key ) ).findFirst();

        return objKey.isPresent() ? String.valueOf( prop.get( objKey.get() ) ) : null ;
    }

    private Integer getIntValue(Properties prop, String key){

        final String strValue = getStrValue( prop, key );
        Integer value = null;

        if(NumberUtils.isDigits( strValue ) ){
            value = Integer.valueOf( strValue );
        }

        return value;
    }
}


