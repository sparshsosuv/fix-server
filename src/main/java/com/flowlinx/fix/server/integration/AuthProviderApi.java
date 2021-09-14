package com.flowlinx.fix.server.integration;

import com.flowlinx.fix.server.integration.dto.AuthProviderLoginDTO;
import com.flowlinx.fix.server.integration.dto.AuthProviderLoginResponseDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface AuthProviderApi {

   @RequestLine("POST /auth")
   @Headers("Content-Type: application/json")
   AuthProviderLoginResponseDTO login(AuthProviderLoginDTO login);

   @RequestLine("POST /auth/logout")
   @Headers({"Content-Type: application/json", "Authorization: {token}"})
   void logout(@Param("token") String token);

}
