package com.flowlinx.fix.server.config.security;

import com.flowlinx.fix.server.integration.dto.AuthProviderLoginResponseDTO;
import com.flowlinx.fix.server.type.Authority;
import lombok.Getter;

import java.security.Principal;
import java.util.Set;

@Getter
public class Profile implements Principal {

   private Long id;
   private String email;
   private String idJwtToken;
   private String firstName;
   private String lastName;
   private Set<Authority> authorities;

   public Profile(AuthProviderLoginResponseDTO dto) {
      this.idJwtToken = dto.getToken();
      this.id = dto.getId();
      this.email = dto.getEmail();
      this.firstName = dto.getFirstName();
      this.lastName = dto.getLastName();
      this.authorities = dto.getAuthorities();
   }

   @Override
   public String getName() {
      return this.email;
   }


}
