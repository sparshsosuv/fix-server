package com.flowlinx.fix.server.integration.dto;

import com.flowlinx.fix.server.type.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthProviderLoginResponseDTO {

   private Long id;
   private String firstName;
   private String lastName;
   private String token;
   private Set<Authority> authorities;
   private String email;

}
