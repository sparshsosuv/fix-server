package com.flowlinx.fix.server.integration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class AuthProviderLoginDTO implements Serializable{

	private static final long serialVersionUID = 208244315187750678L;
	private String login;
    private String password;
    private String product;

	public AuthProviderLoginDTO(String login, String password, String product) {
		super();
		this.login = login;
		this.password = password;
		this.product = product;
	}

}
