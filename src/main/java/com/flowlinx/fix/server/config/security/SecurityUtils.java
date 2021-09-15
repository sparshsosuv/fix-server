package com.flowlinx.fix.server.config.security;

import com.flowlinx.fix.server.exception.AuthorizationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

   public static Profile getProfile() {
      final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

      Profile profile = null;
      if (auth != null && auth.getPrincipal() instanceof Profile) {
         profile =  (Profile) auth.getPrincipal();
      }

      return Optional.of(profile).orElseThrow( () -> new AuthorizationException("Unauthorized") );
   }

   public static Long getUserId() {
	   return getProfile().getId();
   }

}
