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
@Table( name = "log_outgoing_messages")
@IdClass(FixOutgoingMessage.PK.class)
public class FixOutgoingMessage implements FixEntity {

    @Id
    @Column(name = "time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime time;

    @Id
    @Column(name = "beginstring")
    private String beginstring;

    @Column(name = "sendercompid")
    private String sendercompid;

    @Column(name = "sendersubid")
    private String sendersubid;

    @Column(name = "senderlocid")
    private String senderlocid;

    @Column(name = "targetcompid")
    private String targetcompid;

    @Column(name = "targetsubid")
    private String targetsubid;

    @Column(name = "targetlocid")
    private String targetlocid;

    @Column(name = "session_qualifier")
    private String sessionqualifier;

    @Column(name = "text")
    private String text;

    @Transient
    private String msgType;

    @Data
    public static class PK implements Serializable {
        private LocalDateTime time;
        private String beginstring;
    }

}