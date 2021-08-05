package com.flowlinx.fix.server.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table( name = "messages")
@IdClass(FixMessage.FixMessagePK.class)
public class FixMessage {

    @Column(name = "time")
    private LocalDateTime createdAt;

    @Id
    @Column(name = "beginstring")
    private String beginstring;

    @Id
    @Column(name = "sendercompid")
    private String sendercompid;

    @Column(name = "sendersubid")
    private String sendersubid;

    @Id
    @Column(name = "senderlocid")
    private String senderlocid;

    @Id
    @Column(name = "targetcompid")
    private String targetcompid;

    @Id
    @Column(name = "targetsubid")
    private String targetsubid;

    @Id
    @Column(name = "targetlocid")
    private String targetlocid;

    @Id
    @Column(name = "session_qualifier")
    private String session_qualifier;

    @Id
    @Column(name = "msgseqnum")
    private Integer msgseqnum;

    @Column(name = "message")
    private String message;

    @Data
    public static class FixMessagePK implements Serializable {

        private String beginstring;
        private String sendercompid;
        private String sendersubid;
        private String senderlocid;
        private String targetcompid;
        private String targetsubid;
        private String targetlocid;
        private String session_qualifier;
        private Integer msgseqnum;

    }

}