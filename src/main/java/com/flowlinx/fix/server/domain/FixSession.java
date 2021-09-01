package com.flowlinx.fix.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table( name = "sessions")
@IdClass(FixSession.PK.class)
public class FixSession implements Serializable {

    @Id
    @Column(name = "creation_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm::ss.SSS")
    private LocalDateTime time;

    @Id
    @Column(name = "beginstring")
    private String beginstring;

    @Id
    @Column(name = "sendercompid")
    private String sendercompid;

    @Id
    @Column(name = "targetcompid")
    private String targetcompid;

    @Column(name = "sendersubid")
    private String sendersubid;

    @Column(name = "senderlocid")
    private String senderlocid;

    @Column(name = "targetsubid")
    private String targetsubid;

    @Column(name = "targetlocid")
    private String targetlocid;

    @Column(name = "session_qualifier")
    private String sessionqualifier;

    @Column(name = "incoming_seqnum")
    private Integer incomingSeqnum;

    @Column(name = "outgoing_seqnum")
    private Integer outgoingSeqnum;

    @Data
    public static class PK implements Serializable {
        private LocalDateTime time;
        private String beginstring;
        private String sendercompid;
        private String targetcompid;
    }

}