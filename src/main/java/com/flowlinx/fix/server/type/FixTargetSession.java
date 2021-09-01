package com.flowlinx.fix.server.type;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum FixTargetSession {
    FLX_SIMULATOR( FixSenderSession.FLX_SERVER ),
    RECAUAT( FixSenderSession.FLOWLINXUAT ),

    //prod
    RENCAP( FixSenderSession.FLOWLINX );

    FixSenderSession sender;

    FixTargetSession(FixSenderSession sender){
        this.sender = sender;
    }

    public static Optional<FixTargetSession> from(String name){
        return Arrays.stream(FixTargetSession.values()).filter(v -> v.name().equalsIgnoreCase( name ) ).findFirst();
    }

}
