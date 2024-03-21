package com.flowlinx.fix.server.type;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * NEVER ADD FLX_WORKFLOW HERE, IT'S ONLY FOR EXTERNAL FIX CONNECTIONS IDS
 */
@Getter
public enum FixTargetSession {
    FLX_SIMULATOR( FixSenderSession.FLX_SERVER),
    JD_STOCK( FixSenderSession.FLX_SERVER );

    FixSenderSession sender;

    FixTargetSession(FixSenderSession sender){
        this.sender = sender;
    }

    public static Optional<FixTargetSession> from(String name){
        return Arrays.stream(FixTargetSession.values()).filter(v -> v.name().equalsIgnoreCase( name ) ).findFirst();
    }

}
