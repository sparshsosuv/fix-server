package com.flowlinx.fix.server;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import quickfix.*;

import java.util.concurrent.atomic.AtomicInteger;

@EnableQuickFixJServer
@SpringBootApplication
@EnableWebMvc
@ComponentScan("com.flowlinx.fix.server")
@EntityScan("com.flowlinx.fix.server.persistence")
@EnableJpaRepositories("com.flowlinx.fix.server.persistence")
public class AppServer {

    private static final Logger log = LoggerFactory.getLogger(AppServer.class);

    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static void main(String[] args) {
        SpringApplication.run(AppServer.class, args);
    }

    @Bean
    public Initiator serverInitiator(ServerApplicationAdapter serverApplicationAdapter, MessageStoreFactory serverMessageStoreFactory,
                                   SessionSettings serverSessionSettings, LogFactory serverLogFactory,
                                   MessageFactory serverMessageFactory) throws ConfigError {

        return new ThreadedSocketInitiator(serverApplicationAdapter, serverMessageStoreFactory, serverSessionSettings,
                serverLogFactory, serverMessageFactory);

    }

}
