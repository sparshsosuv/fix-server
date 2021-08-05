package com.flowlinx.fix.server;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import quickfix.*;

@EnableQuickFixJServer
@SpringBootApplication
@EnableWebMvc
@ComponentScan("com.flowlinx.fix.server")
public class AppServer {

    public static void main(String[] args) {
		SpringApplication.run(AppServer.class, args);
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

	@Bean
	public MessageStoreFactory serverMessageStoreFactory(SessionSettings serverSessionSettings) {
		return new JdbcStoreFactory(serverSessionSettings);
	}

	@Bean
	public LogFactory serverLogFactory(SessionSettings serverSessionSettings) {
		return new JdbcLogFactory(serverSessionSettings);
	}

}
