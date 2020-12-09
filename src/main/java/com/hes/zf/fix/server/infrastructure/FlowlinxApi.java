package com.hes.zf.fix.server.infrastructure;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

@Headers("Content-Type: application/json")
public interface FlowlinxApi {

    @RequestLine("POST /auth/login")
    @Headers("Content-Type: application/json")
    UserRepresentation login(LoginRepresentation login);

    @RequestLine("POST /file/parser/ioi")
    @Headers("Authorization: Bearer {token}")
    void send(OrderRepresentation order, @Param("token") String token);

    @RequestLine("POST /file/parser/iois")
    @Headers("Authorization: Bearer {token}")
    void send(List<OrderRepresentation> orders, @Param("token") String token);

}