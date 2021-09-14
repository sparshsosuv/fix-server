package com.flowlinx.fix.server.config.security.jwt;

import com.flowlinx.fix.server.config.security.Profile;
import com.flowlinx.fix.server.integration.dto.AuthProviderLoginResponseDTO;
import com.flowlinx.fix.server.type.Authority;
import com.flowlinx.fix.server.utils.AppConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

	private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

	private static final SimpleGrantedAuthority ADMIN_ROLE = new SimpleGrantedAuthority(Authority.ROLE_ADMIN.name());
	private static final String AUTHORITIES_KEY = "auth";
	private static final String FIRST_NAME_KEY = "FIRST_NAME_KEY";
	private static final String LAST_NAME_KEY = "LAST_NAME_KEY";

	private Key key;

	@Value("${jwt.base64-secret}")
	private String base64Secret;

	@Value("${jwt.token-validity-in-seconds}")
	private Long tokenValidityInMilliseconds;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(Authentication authentication, boolean rememberMe) {

		final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		final long now = (new Date()).getTime();
		final Date validity = new Date(now + this.tokenValidityInMilliseconds);

		final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		final Profile profile = (Profile) auth.getPrincipal();

		return Jwts.builder().setSubject(authentication.getName())
				.setId(profile.getId().toString())
				.setIssuer(AppConstants.JWT_ISSUER)
				.claim(AUTHORITIES_KEY, authorities)
				.claim(FIRST_NAME_KEY, profile.getFirstName() )
				.claim(LAST_NAME_KEY, profile.getLastName() )
				.signWith(key, SignatureAlgorithm.HS384)
				.setExpiration(validity).compact();
	}

	public void authenticate(String token) {
		final Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

		final Collection<? extends GrantedAuthority> authorities = Arrays
				.stream( claims.get( AUTHORITIES_KEY ).toString().split(",") )
				.map( SimpleGrantedAuthority::new )
				.collect( Collectors.toList() );

		final User principal = new User( claims.getSubject(), "", authorities);

		final Set<Authority> authoritySet = Arrays
				.stream( claims.get( AUTHORITIES_KEY ).toString().split(",") )
				.map( Authority::from )
				.collect( Collectors.toSet() );

		final AuthProviderLoginResponseDTO dto = new AuthProviderLoginResponseDTO();
		dto.setAuthorities( authoritySet );
		dto.setEmail( claims.getSubject() );
		dto.setId( new Long( claims.getId() ) );
		dto.setFirstName( claims.get( FIRST_NAME_KEY ).toString() );
		dto.setLastName( claims.get( LAST_NAME_KEY ).toString() );

		Authentication auth = new UsernamePasswordAuthenticationToken(
				new Profile( dto ), principal.getPassword(), authorities);

		SecurityContextHolder.getContext().setAuthentication( auth );
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace: {}", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace: {}", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace: {}", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace: {}", e);
		}
		return false;
	}
}
