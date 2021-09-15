package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.config.security.Profile;
import com.flowlinx.fix.server.config.security.jwt.TokenProvider;
import com.flowlinx.fix.server.resource.representation.LoginRepresentation;
import com.flowlinx.fix.server.utils.AppConstants;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authBuilder;

	public AuthenticationResource(TokenProvider tokenProvider,
                                  AuthenticationManagerBuilder authBuilder) {
		this.tokenProvider = tokenProvider;
		this.authBuilder = authBuilder;
	}

	@PostMapping
	public ResponseEntity<JWTToken> authorize(@RequestBody LoginRepresentation representation, HttpServletRequest request) {
		final Authentication credentials = new UsernamePasswordAuthenticationToken(representation.getLogin(),
				representation.getPassword());

      return authorize( credentials, representation.isRememberMe(), request);
	}

   @PostMapping("/logout")
   public void logout(HttpServletResponse response) {

//      UserAccount userAccount = userService.findById( SecurityUtils.getProfile().getId() )
//         .orElseThrow( () -> new ResourceNotFoundException( SecurityUtils.getProfile().getId() ) );
//
//      userSessionActivity.logout( userAccount );

   }

   private ResponseEntity<JWTToken> authorize(Authentication credentials, boolean rememberMe, HttpServletRequest request) {

      Authentication authentication = authBuilder.getObject().authenticate( credentials );
      SecurityContextHolder.getContext().setAuthentication(authentication);

      final String token = tokenProvider.createToken(authentication, rememberMe);

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add( AppConstants.HEADER_AUTHORIZATION, "Bearer " + token );

      final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
      final Profile profile = (Profile) auth.getPrincipal();

      final List<String> authorities = authentication.getAuthorities()
         .stream().map(GrantedAuthority::getAuthority).collect( Collectors.toList() );

      final JWTToken jwt = JWTToken.builder()
         .id( profile.getId() )
         .firstName( profile.getFirstName() )
         .lastName( profile.getLastName() )
         .email( profile.getEmail() )
         .authorities( authorities )
         .token( token )
         .idJwtToken( profile.getIdJwtToken() )
         .build();

      return new ResponseEntity<>( jwt, httpHeaders, HttpStatus.OK );
   }

	@Builder @Getter
	static class JWTToken {
      private Long id;
      private String firstName;
      private String lastName;
      private String email;
      private List<String> authorities;
      private String token;
      private String idJwtToken;
      private String tenantId;
   }
}
