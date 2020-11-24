package io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure;

import com.google.gson.Gson;
import feign.Feign;
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

public class FlowlinxApiClient {


    private static final Logger LOGGER = LoggerFactory.getLogger( FlowlinxApiClient.class );

    @Value("${api.base.url}")
    private String baseUrl;

    @Autowired
    private Gson gson;

    private FlowlinxApi fullLogApi;

    @PostConstruct
    public void init() {

        fullLogApi = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder( gson ) )
                .decoder(new GsonDecoder( gson ) )
                .logger( new Slf4jLogger( FlowlinxApi.class ) )
                .logLevel( feign.Logger.Level.FULL )
                .target( FlowlinxApi.class, baseUrl );

    }

    public UserRepresentation auth( String login, String password ) {

        final FlowlinxApi api = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger( new Slf4jLogger( FlowlinxApi.class ) )
                .logLevel( feign.Logger.Level.BASIC )
                .target( FlowlinxApi.class, baseUrl );

        return api.login( new LoginRepresentation( login, password ) );

    }

    public void send(OrderRepresentation order, String token) {
        try {

            fullLogApi.send( order, token );
        }catch (FeignException e) {
            LOGGER.error( order + " was not found on the platform.");
            e.printStackTrace();
        }
    }

    public void send(List<OrderRepresentation> orders, String token) {
        try {

            fullLogApi.send( orders, token );
        }catch (FeignException e) {
            LOGGER.error( orders.toString() + " was not found on the platform.");
            e.printStackTrace();
        }
    }

}
