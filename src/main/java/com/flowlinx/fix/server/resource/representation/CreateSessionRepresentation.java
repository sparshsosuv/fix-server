package com.flowlinx.fix.server.resource.representation;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CreateSessionRepresentation {

    private String startTime;
    private String endTime;
    private String heartBtInt;
    private String validOrderTypes;
    private String useDataDictionary;
    private String defaultMarketPrice;
    private String validateUserDefinedFields;
    private String validateFieldsOutOfOrder;
    private String validateFieldsHaveValue;
    private String persistMessages;
    private String resetOnLogout;
    private String resetOnDisconnect;
    private String beginString;
    private String connectionType;

    private String socketConnectHost;
    private String socketConnectPort;
    private String senderCompID;
    private String targetCompID;
    private String dataDictionary;

    private String socketAcceptPort;
}
