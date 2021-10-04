package com.flowlinx.fix.server.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table( name = "routing_table")
public class RoutingTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deliver_to_comp_id")
    private String deliverToCompID;

    @Column(name = "sender_comp_id")
    private String senderCompID;

    @Column(name = "target_comp_id")
    private String targetCompID;

}