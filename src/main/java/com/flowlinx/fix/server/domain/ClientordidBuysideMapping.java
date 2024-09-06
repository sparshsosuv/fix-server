package com.flowlinx.fix.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter @ToString
@Entity
@Table( name = "clientordid_buyside_mapping")
public class ClientordidBuysideMapping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_order_id")
    private String clientOrderId;

    @Column(name = "buy_side_session")
    private String buySideSession;

    @Column(name = "message")
    private String message;

    @Column(name = "msg_type")
    private String msgType;

    @Column(name = "msg_seq_num")
    private Long msgSeqNum;

    @Column(name = "sender")
    private String sender;

    @Column(name = "target")
    private String target;

    @Column(name = "fix_version")
    private String fixVersion;
}