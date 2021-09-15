package com.flowlinx.fix.server.integration;

import com.flowlinx.fix.server.integration.dto.AuthProviderLoginDTO;
import com.flowlinx.fix.server.integration.dto.AuthProviderLoginResponseDTO;
import com.google.gson.Gson;
import feign.Feign;
import feign.Logger.Level;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthProviderApiClient {

   private static final String PRODUCT = "ID";

   @Value("${api.auth.provider.base.url}")
   private String baseUrl;

   @Autowired
   private Gson gson;

   public AuthProviderLoginResponseDTO login(String login, String password) {

      final AuthProviderApi api = Feign.builder()
         .client( new OkHttpClient() )
         .encoder( new GsonEncoder( gson ) )
         .decoder( new GsonDecoder( gson ) )
         .logLevel( Level.FULL )
         .target( AuthProviderApi.class, baseUrl );

      return api.login( new AuthProviderLoginDTO( login, password, PRODUCT) );
   }

   public void logout(String token) {

      final AuthProviderApi api = Feign.builder()
         .client(new OkHttpClient())
         .encoder( new GsonEncoder( gson ) )
         .decoder( new GsonDecoder( gson ) )
         .logLevel( Level.FULL )
         .target( AuthProviderApi.class, baseUrl );

      api.logout( "Bearer " + token );
   }

}
