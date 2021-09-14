package com.flowlinx.fix.server.config.security.provider;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class IdFlowlinxToken implements Authentication {

   final String idJwtToken;

   public IdFlowlinxToken(String idJwtToken){
      this.idJwtToken = idJwtToken;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return null;
   }

   @Override
   public Object getCredentials() {
      return null;
   }

   @Override
   public Object getDetails() {
      return null;
   }

   @Override
   public Object getPrincipal() {
      return null;
   }

   @Override
   public boolean isAuthenticated() {
      return false;
   }

   @Override
   public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

   }

   @Override
   public String getName() {
      return null;
   }
}
