//package com.flowlinx.fix.server.domain;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.*;
//import java.sql.Timestamp;
//
//@Getter @Setter @ToString
//@Entity
//@Table(name = "fixsession")
//public class FixSessionExt {
//
//    @Id
//    @Column(name = "sessionid", length = 100, nullable = false)
//    private String sessionId;
//
//    @Column(name = "firmname", length = 100, nullable = false)
//    private String firmName;
//
//    @Column(name = "sendercompid", length = 100, nullable = false)
//    private String senderCompId;
//
//    @Column(name = "connectiontype", length = 100, nullable = false)
//    private String connectionType;
//
//    @Column(name = "targetcompid", length = 100, nullable = false)
//    private String targetCompId;
//
//    @Column(name = "starttime", length = 50, nullable = false)
//    private String startTime;
//
//    @Column(name = "endtime", length = 50, nullable = false)
//    private String endTime;
//
//    @Column(name = "ipaddress", length = 50, nullable = false)
//    private String ipAddress;
//
//    @Column(name = "port", nullable = false)
//    private Long port;
//
//    @Column(name = "fixuser", length = 100, nullable = false)
//    private String fixUser;
//
//    @Column(name = "fixpassword", length = 100, nullable = false)
//    private String fixPassword;
//
//    @Column(name = "heartbeatinterval", nullable = false)
//    private Long heartbeatInterval;
//
//    @Column(name = "fixversion", length = 10, nullable = false)
//    private String fixVersion;
//
//    @Column(name = "sendersubid", length = 100)
//    private String senderSubId;
//
//    @Column(name = "senderlocationid", length = 100)
//    private String senderLocationId;
//
//    @Column(name = "targetsubid", length = 100)
//    private String targetSubId;
//
//    @Column(name = "targetlocationid", length = 100)
//    private String targetLocationId;
//
//    @Column(name = "applicationtype", length = 50, nullable = false)
//    private String applicationType;
//
//    @Column(name = "datadictionary", length = 100, nullable = false)
//    private String dataDictionary;
//
//    @Column(name = "counterparty_type", length = 20, nullable = false)
//    private String counterpartyType;
//
//    @Column(name = "routingtag", length = 50)
//    private String routingTag;
//
//    @Column(name = "timestamp")
//    private Timestamp timestamp;
//
//    @Column(name = "session_status", length = 5)
//    private String sessionStatus;
//
//    @Column(name = "usedatadictionary")
//    private String useDataDictionary;
//
//    @Column(name = "instance", length = 200)
//    private String instance;
//
//    @Column(name = "region", length = 100)
//    private String region;
//
//    @Column(name = "hub", length = 100)
//    private String hub;
//
//    @Column(name = "timezone", length = 10)
//    private String timeZone;
//
//    @Column(name = "displaystarttime", length = 50)
//    private String displayStartTime;
//
//    @Column(name = "displayendtime", length = 50)
//    private String displayEndTime;
//
//    @Column(name = "normalization")
//    private Boolean normalization;
//
//    @Column(name = "last_updated", nullable = false)
//    private Timestamp lastUpdated;
//
//}

package com.flowlinx.fix.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Entity
@Table(name = "fixsession")
@IdClass(FixSessionId.class)
public class FixSessionExt {

    @Id
    @Column(name = "sessionid", length = 100, nullable = false)
    private String sessionId;

    @Id
    @Column(name = "fixversion", length = 10, nullable = false)
    private String fixVersion;

    @Column(name = "firmname", length = 100, nullable = false)
    private String firmName;

    @Column(name = "sendercompid", length = 100, nullable = false)
    private String senderCompId;

    @Column(name = "connectiontype", length = 100, nullable = false)
    private String connectionType;

    @Column(name = "targetcompid", length = 100, nullable = false)
    private String targetCompId;

    @Column(name = "starttime", length = 50, nullable = false)
    private String startTime;

    @Column(name = "endtime", length = 50, nullable = false)
    private String endTime;

    @Column(name = "ipaddress", length = 50, nullable = false)
    private String ipAddress;

    @Column(name = "port", nullable = false)
    private Long port;

    @Column(name = "fixuser", length = 100, nullable = false)
    private String fixUser;

    @Column(name = "fixpassword", length = 100, nullable = false)
    private String fixPassword;

    @Column(name = "heartbeatinterval", nullable = false)
    private Long heartbeatInterval;

    @Column(name = "sendersubid", length = 100)
    private String senderSubId;

    @Column(name = "senderlocationid", length = 100)
    private String senderLocationId;

    @Column(name = "targetsubid", length = 100)
    private String targetSubId;

    @Column(name = "targetlocationid", length = 100)
    private String targetLocationId;

    @Column(name = "applicationtype", length = 50, nullable = false)
    private String applicationType;

    @Column(name = "datadictionary", length = 100, nullable = false)
    private String dataDictionary;

    @Column(name = "counterparty_type", length = 20, nullable = false)
    private String counterpartyType;

    @Column(name = "routingtag", length = 50)
    private String routingTag;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "session_status", length = 20)
    private String sessionStatus;

    @Column(name = "usedatadictionary")
    private Boolean useDataDictionary;

    @Column(name = "instance", length = 200)
    private String instance;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "hub", length = 100)
    private String hub;

    @Column(name = "timezone", length = 10)
    private String timeZone;

    @Column(name = "displaystarttime", length = 50)
    private String displayStartTime;

    @Column(name = "displayendtime", length = 50)
    private String displayEndTime;

    @Column(name = "normalization")
    private Boolean normalization;

    @Column(name = "normalization_clients")
    private String normalizationClients;

    @Column(name = "last_updated", nullable = false)
    private Timestamp lastUpdated;
}


