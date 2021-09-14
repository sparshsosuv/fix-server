package com.flowlinx.fix.server.config.security.jwt;

import com.flowlinx.fix.server.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal
 * if a header corresponding to a valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

   private static final Logger LOG = LoggerFactory.getLogger(JWTFilter.class);


   private TokenProvider tokenProvider;

   public JWTFilter(TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
   }

   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

	   final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      final String jwt = resolveToken( httpServletRequest );
      final String requestURI = httpServletRequest.getRequestURI();

      if ( StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) ) {
    	 tokenProvider.authenticate( jwt );

      } else {
         LOG.debug("no valid JWT token found, uri: {}", requestURI);
      }

      filterChain.doFilter( servletRequest, servletResponse );
   }

   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader(AppConstants.HEADER_AUTHORIZATION );
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;
   }


}
