package com.flowlinx.fix.server.infrastructure;

import java.io.Serializable;

public class UserRepresentation implements Serializable{

    private static final long serialVersionUID = 7119688623108049876L;
    private Long id;
    private String login;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
