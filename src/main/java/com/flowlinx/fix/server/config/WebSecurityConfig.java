package com.flowlinx.fix.server.config;

import com.flowlinx.fix.server.config.security.JwtAccessDeniedHandler;
import com.flowlinx.fix.server.config.security.JwtAuthenticationEntryPoint;
import com.flowlinx.fix.server.config.security.jwt.JWTConfigurer;
import com.flowlinx.fix.server.config.security.jwt.TokenProvider;
import com.flowlinx.fix.server.config.security.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

   private static final String[] AUTH_WHITELIST = { "/auth/**", "/public/**" };

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Configuration
   public class GlobalSecurityConfig extends WebSecurityConfigurerAdapter {

      private final TokenProvider tokenProvider;
      private final CorsFilter corsFilter;
      private final JwtAuthenticationEntryPoint authenticationErrorHandler;
      private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
      private final CustomAuthenticationProvider customAuthenticationProvider;

      public GlobalSecurityConfig(
         TokenProvider tokenProvider,
         CorsFilter corsFilter,
         JwtAuthenticationEntryPoint authenticationErrorHandler,
         JwtAccessDeniedHandler jwtAccessDeniedHandler,
         CustomAuthenticationProvider customAuthenticationProvider) {
         this.tokenProvider = tokenProvider;
         this.corsFilter = corsFilter;
         this.authenticationErrorHandler = authenticationErrorHandler;
         this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
         this.customAuthenticationProvider = customAuthenticationProvider;
      }

      @Override
      @Autowired
      public void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.authenticationProvider(customAuthenticationProvider);
      }

      // Configure paths and requests that should be ignored by Spring Security ================================

      @Override
      public void configure(WebSecurity web) {
         web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            // allow anonymous resource requests
            .antMatchers(
               "/",
               "/*.html",
               "/favicon.ico",
               "/**/*.html",
               "/**/*.css",
               "/**/*.js"
            );
      }

      // Configure security settings ===========================================================================

      @Override
      protected void configure(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
            // we don't need CSRF because our token is invulnerable
            .csrf().disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationErrorHandler)
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            // create no session
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers( AUTH_WHITELIST ).permitAll()
            .anyRequest().authenticated()
            .and()
            .apply(securityConfigurerAdapter());
      }

      private JWTConfigurer securityConfigurerAdapter() {
         return new JWTConfigurer(tokenProvider);
      }
   }


}
