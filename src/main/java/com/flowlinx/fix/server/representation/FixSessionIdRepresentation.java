package com.flowlinx.fix.server.representation;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class FixSessionIdRepresentation implements Serializable {

    private String id;
    private String beginString;
    private String senderCompID;
    private String senderSubID;
    private String senderLocationID;
    private String targetCompID;
    private String targetSubID;
    private String targetLocationID;
    private String sessionQualifier;
    private String connectionType;
}
