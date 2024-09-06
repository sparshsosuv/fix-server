package com.flowlinx.fix.server.type;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * NEVER ADD FLX_WORKFLOW HERE, IT'S ONLY FOR EXTERNAL FIX CONNECTIONS IDS
 */
@Getter
public enum FixTargetSession {
//    SOSUV_SIMULATOR( FixSenderSession.SOSUV_SERVER),
    CLIENT42( FixSenderSession.SOSUV_SERVER),
    CLIENT44( FixSenderSession.SOSUV_SERVER_44),
    SOSUV_VAYU( FixSenderSession.SOSUV_SERVER),
    SOSUV_VAYU_44( FixSenderSession.SOSUV_SERVER_44),
    JD_STOCK( FixSenderSession.SOSUV_SERVER ),
    BANZAI( FixSenderSession.SOSUV_SERVER);

    FixSenderSession sender;

    FixTargetSession(FixSenderSession sender){
        this.sender = sender;
    }

    public static Optional<FixTargetSession> from(String name){
        return Arrays.stream(FixTargetSession.values()).filter(v -> v.name().equalsIgnoreCase( name ) ).findFirst();
    }

}
