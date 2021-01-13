package com.flowlinx.fix.server.infrastructure;

import java.io.Serializable;

public class LoginRepresentation implements Serializable {

    private static final long serialVersionUID = 208244315187750678L;
    private String login;
    private String password;

    public LoginRepresentation(String login, String password) {
        super();
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
