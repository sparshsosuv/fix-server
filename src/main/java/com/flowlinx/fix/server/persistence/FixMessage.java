package com.flowlinx.fix.server.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class FixMessage{

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "message")
    private String message;

    public FixMessage(String message){
        this.createdAt = LocalDateTime.now();

        if( message != null ){
            this.message = message.replaceAll("\u0001", " ");
        }
    }

}