//package com.flowlinx.fix.server.config;
//
//import com.flowlinx.fix.server.fix.ClientApplicationAdapter;
//import com.flowlinx.fix.server.fix.ServerApplicationAdapter;
//import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJClient;
//import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import quickfix.*;
//
//@Configuration
//@EnableQuickFixJServer
//@EnableQuickFixJClient
//public class FixConfig {
//
//	@Bean
//	public ThreadedSocketAcceptor serverAcceptor(ServerApplicationAdapter serverApplication, MessageStoreFactory serverMessageStoreFactory,
//								   SessionSettings serverSessionSettings, LogFactory serverLogFactory,
//								   MessageFactory serverMessageFactory) throws ConfigError {
//
//		return new ThreadedSocketAcceptor(serverApplication, serverMessageStoreFactory, serverSessionSettings,
//				serverLogFactory, serverMessageFactory);
//	}
//
//	@Bean
//	public ThreadedSocketInitiator clientInitiator(ClientApplicationAdapter clientApplicationAdapter, MessageStoreFactory clientMessageStoreFactory,
//									 SessionSettings clientSessionSettings, LogFactory clientLogFactory,
//									 MessageFactory clientMessageFactory) throws ConfigError {
//
//		return new ThreadedSocketInitiator(clientApplicationAdapter, clientMessageStoreFactory, clientSessionSettings,
//				clientLogFactory, clientMessageFactory);
//	}
//
//	@Bean
//	public MessageStoreFactory serverMessageStoreFactory(SessionSettings serverSessionSettings) {
//		return new JdbcStoreFactory(serverSessionSettings);
//	}
//
//	@Bean
//	public LogFactory serverLogFactory(SessionSettings serverSessionSettings) {
//		return new JdbcLogFactory(serverSessionSettings);
//	}
//
//	@Bean
//	public SessionFactory sessionFactory(ServerApplicationAdapter serverApplication, MessageStoreFactory serverMessageStoreFactory,
//										 LogFactory serverLogFactory, MessageFactory serverMessageFactory) {
//		return new DefaultSessionFactory(serverApplication, serverMessageStoreFactory, serverLogFactory, serverMessageFactory);
//	}
//
//}



//import com.flowlinx.fix.server.fix.ClientApplicationAdapter;
//import com.flowlinx.fix.server.fix.ServerApplicationAdapter;
//import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJClient;
//import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import quickfix.*;
//
//@Configuration
//public class FixConfig {
//
//	@Configuration
//	@EnableQuickFixJServer
//	@ConditionalOnProperty(name = "quickfixj.server.enabled", havingValue = "true")
//	public static class QuickFixJServerConfig {
//
//		@Bean
//		public ThreadedSocketAcceptor serverAcceptor(ServerApplicationAdapter serverApplication, MessageStoreFactory serverMessageStoreFactory,
//													 SessionSettings serverSessionSettings, LogFactory serverLogFactory,
//													 MessageFactory serverMessageFactory) throws ConfigError {
//
//			return new ThreadedSocketAcceptor(serverApplication, serverMessageStoreFactory, serverSessionSettings,
//					serverLogFactory, serverMessageFactory);
//		}
//
//		@Bean
//		public MessageStoreFactory serverMessageStoreFactory(SessionSettings serverSessionSettings) {
//			return new JdbcStoreFactory(serverSessionSettings);
//		}
//
//		@Bean
//		public LogFactory serverLogFactory(SessionSettings serverSessionSettings) {
//			return new JdbcLogFactory(serverSessionSettings);
//		}
//
//		@Bean
//		public SessionFactory sessionFactory(ServerApplicationAdapter serverApplication, MessageStoreFactory serverMessageStoreFactory,
//											 LogFactory serverLogFactory, MessageFactory serverMessageFactory) {
//			return new DefaultSessionFactory(serverApplication, serverMessageStoreFactory, serverLogFactory, serverMessageFactory);
//		}
//	}
//
//	@Configuration
//	@EnableQuickFixJClient
//	@ConditionalOnProperty(name = "quickfixj.client.enabled", havingValue = "true")
//	public static class QuickFixJClientConfig {
//
//		@Bean
//		public ThreadedSocketInitiator clientInitiator(ClientApplicationAdapter clientApplicationAdapter, MessageStoreFactory clientMessageStoreFactory,
//													   SessionSettings clientSessionSettings, LogFactory clientLogFactory,
//													   MessageFactory clientMessageFactory) throws ConfigError {
//
//			return new ThreadedSocketInitiator(clientApplicationAdapter, clientMessageStoreFactory, clientSessionSettings,
//					clientLogFactory, clientMessageFactory);
//		}
//	}
//}



package com.flowlinx.fix.server.config;

import com.flowlinx.fix.server.fix.ClientApplicationAdapter;
import com.flowlinx.fix.server.fix.ServerApplicationAdapter;
import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJClient;
import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import quickfix.*;

@Configuration
@EnableQuickFixJServer
@EnableQuickFixJClient
public class FixConfig {

	@Bean
	@Conditional(OnServerEnabledCondition.class)
	public ThreadedSocketAcceptor serverAcceptor(ServerApplicationAdapter serverApplication, MessageStoreFactory serverMessageStoreFactory,
												 SessionSettings serverSessionSettings, LogFactory serverLogFactory,
												 MessageFactory serverMessageFactory) throws ConfigError {

		return new ThreadedSocketAcceptor(serverApplication, serverMessageStoreFactory, serverSessionSettings,
				serverLogFactory, serverMessageFactory);
	}

	@Bean
	@Conditional(OnClientEnabledCondition.class)
	public ThreadedSocketInitiator clientInitiator(ClientApplicationAdapter clientApplicationAdapter, MessageStoreFactory clientMessageStoreFactory,
												   SessionSettings clientSessionSettings, LogFactory clientLogFactory,
												   MessageFactory clientMessageFactory) throws ConfigError {

		return new ThreadedSocketInitiator(clientApplicationAdapter, clientMessageStoreFactory, clientSessionSettings,
				clientLogFactory, clientMessageFactory);
	}

	@Bean
	public MessageStoreFactory serverMessageStoreFactory(SessionSettings serverSessionSettings) {
		return new JdbcStoreFactory(serverSessionSettings);
	}

	@Bean
	public LogFactory serverLogFactory(SessionSettings serverSessionSettings) {
		return new JdbcLogFactory(serverSessionSettings);
	}

	@Bean
	public SessionFactory sessionFactory(ServerApplicationAdapter serverApplication, MessageStoreFactory serverMessageStoreFactory,
										 LogFactory serverLogFactory, MessageFactory serverMessageFactory) {
		return new DefaultSessionFactory(serverApplication, serverMessageStoreFactory, serverLogFactory, serverMessageFactory);
	}
}
