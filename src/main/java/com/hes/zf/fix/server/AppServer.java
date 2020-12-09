package com.hes.zf.fix.server;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import quickfix.*;

@EnableQuickFixJServer
@SpringBootApplication
public class AppServer {


    private static final Logger log = LoggerFactory.getLogger(AppServer.class);

    public static void main(String[] args) {
        SpringApplication.run(AppServer.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        log.info("Joining thread, you can press Ctrl+C to shutdown application");
//        //Thread.currentThread().join();
//
//https://www.youtube.com/watch?v=wSD85X5AUQA&ab_channel=LittleMissNasty
//        final ScheduledExecutorService executorService = Executors
//                .newSingleThreadScheduledExecutor();
//
//        //final Message ioi = new OrderStatusRequest(new ClOrdID("456"), new Side(Side.BUY));
//
//
//
//    }

    @Bean
    public Application serverApplication() {
        return new ServerApplicationAdapter();
    }

    @Bean
    public Acceptor serverAcceptor(quickfix.Application serverApplication, MessageStoreFactory serverMessageStoreFactory,
                                   SessionSettings serverSessionSettings, LogFactory serverLogFactory,
                                   MessageFactory serverMessageFactory) throws ConfigError {

        return new ThreadedSocketAcceptor(serverApplication, serverMessageStoreFactory, serverSessionSettings,
                serverLogFactory, serverMessageFactory);

    }
}
