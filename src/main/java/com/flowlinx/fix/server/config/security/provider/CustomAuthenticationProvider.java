package com.flowlinx.fix.server.config.security.provider;

import com.flowlinx.fix.server.config.security.Profile;
import com.flowlinx.fix.server.integration.AuthProviderApiClient;
import com.flowlinx.fix.server.integration.dto.AuthProviderLoginResponseDTO;
import com.flowlinx.fix.server.type.Authority;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

   @Autowired
   private AuthProviderApiClient authProviderApiClient;

   @Override
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {

      final String login = authentication.getName().trim();
      final String password = authentication.getCredentials().toString();

      try {

         final AuthProviderLoginResponseDTO response = authProviderApiClient.login( login, password );

         if( !response.getAuthorities().contains( Authority.ROLE_ADMIN ) ){
            throw new BadCredentialsException("invalid credentials");
         }

         final List<SimpleGrantedAuthority> authorities = response.getAuthorities().stream()
                 .map( a -> new SimpleGrantedAuthority( a.name() ) )
                 .collect( Collectors.toList() );

         return new UsernamePasswordAuthenticationToken(
                 new Profile( response ), password, authorities);

      }catch (FeignException e) {
         throw new BadCredentialsException("invalid credentials");
      }

   }

   @Override
   public boolean supports(Class<?> aClass) {
      return aClass.equals(UsernamePasswordAuthenticationToken.class);
   }

}
