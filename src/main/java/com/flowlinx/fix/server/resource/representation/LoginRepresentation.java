package com.flowlinx.fix.server.resource.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LoginRepresentation implements Serializable {

   private static final long serialVersionUID = 208244315187750678L;
   private String login;
   private String password;
   private boolean rememberMe;

   public LoginRepresentation(String login, String password) {
      super();
      this.login = login;
      this.password = password;
   }

}
