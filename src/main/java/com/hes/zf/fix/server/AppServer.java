package com.hes.zf.fix.server;

import com.hes.zf.fix.server.type.FixSession;
import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.NewOrderSingle;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@EnableQuickFixJServer
@SpringBootApplication
public class AppServer {

    private static final Logger log = LoggerFactory.getLogger(AppServer.class);

    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static void main(String[] args) {
        SpringApplication.run(AppServer.class, args);

        final ScheduledExecutorService executorService = Executors
                .newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate( () -> {
            try {
                log.info("SENDING ORDER FROM SERVER TO DEALING_SHEET");
                Session.sendToTarget( getNewOrderSingle(), "SERVER", FixSession.DEALING_SHEET.name() );
            } catch (SessionNotFound e) {
                e.printStackTrace();
            }

        }, 30, 120, TimeUnit.SECONDS);

    }

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

    private static NewOrderSingle getNewOrderSingle(){

        final ClOrdID clOrdID = new ClOrdID( "1" );
        final Side side = new Side( 'B' );
        final TransactTime transactTime = new TransactTime( LocalDateTime.now() );
        final OrdType ordType = new OrdType( OrdType.MARKET );

        final int counter = COUNTER.getAndIncrement();

        final NewOrderSingle newOrderSingle = new NewOrderSingle( clOrdID, side, transactTime, ordType );
        newOrderSingle.setField( new TimeInForce( TimeInForce.DAY ) );
        newOrderSingle.setField( new Currency( "USD" ) );
        newOrderSingle.setField( new SecurityExchange( "NL" ) );
        newOrderSingle.setField( new IDSource( IDSource.RIC_CODE ) );
        newOrderSingle.setField( new SecurityID( "TEST" ) );
        newOrderSingle.setField( new Symbol( "APPLE " + counter ) );
        newOrderSingle.setField( new HandlInst( HandlInst.MANUAL_ORDER_BEST_EXECUTION ) );
        newOrderSingle.setField( new OrderQty( 1000d ) );
        newOrderSingle.setField( new Price( 90d ) );
        newOrderSingle.setField( new Account( "Account " + counter ) );
        newOrderSingle.setField( new MinQty( 999d ) );
        newOrderSingle.setField( new Text( "TEXT" ) );
        newOrderSingle.setField( new ExpireTime( LocalDateTime.now().plus( Period.ofMonths(1) ) ) );

        //newOrderSingle.setField( new DeliverToCompID( "Test Securities" ) ); // 128
        newOrderSingle.getHeader().setField( new DeliverToCompID( "Test Securities" ) );

        return newOrderSingle;
    }
}
