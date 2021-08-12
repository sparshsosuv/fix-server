package com.flowlinx.fix.server.resource.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import quickfix.Application;
import quickfix.StringField;
import quickfix.UtcTimestampPrecision;
import quickfix.field.ApplVerID;
import quickfix.field.DefaultApplVerID;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter @Setter
public class FixSessionRepresentation implements Serializable {

    private FixSessionIdRepresentation sessionID;
    private int expectedSenderNum;
    private int expectedTargetNum;
    private Date startTime;
    private String remoteAddress;
    private String responderRemoteAddress;
    private int logonTimeout;
    private int logoutTimeout;


    private boolean enabled;
    private long lastSessionTimeCheck;
    private int logonAttempts;
    private long lastSessionLogon;
    private String dictionaryVersion;
    private boolean checkLatency;
    private int maxLatency;
    private int resendRequestChunkSize;
    private boolean resetOnLogon;
    private boolean resetOnLogout;
    private boolean resetOnDisconnect;
    private boolean resetOnError;
    private boolean disconnectOnError;
    private boolean refreshOnLogon;
    private boolean redundantResentRequestsAllowed;
    private boolean persistMessages;
    private boolean checkCompID;
    private boolean useClosedRangeForResend;
    private boolean disableHeartBeatCheck;
    private boolean rejectGarbledMessage;
    private boolean rejectInvalidMessage;
    private boolean rejectMessageOnUnhandledException;
    private boolean requiresOrigSendingTime;
    private boolean forceResendWhenCorruptedStore;
    private boolean enableNextExpectedMsgSeqNum;
    private boolean enableLastMsgSeqNumProcessed;
    private boolean validateChecksum;
    private int maxScheduledWriteRequests;
    private AtomicBoolean isResetting;
    private AtomicBoolean isResettingState;
    private AtomicReference<ApplVerID> targetDefaultApplVerID;
    private DefaultApplVerID senderDefaultApplVerID;
    private boolean validateSequenceNumbers;
    private boolean validateIncomingMessage;
    private int[] logonIntervals;
    private Set<InetAddress> allowedRemoteAddresses;
    private List<StringField> logonTags;
    

}
