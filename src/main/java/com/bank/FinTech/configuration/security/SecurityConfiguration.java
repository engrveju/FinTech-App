package com.bank.FinTech.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  private final String[] WHITE_LISTED_URLS = { "/v2/api-docs/**", "/v3/api-docs/**","/configuration/**",
   "/swagger*/**","/swagger-ui/**","/webjars/**", "/api/v1/customer/signup","/api/v1/customer/verifyRegistration/**",
          "/api/v1/category/**", "/api/v1/subcategory/**", "/api/v1/finalizeTrans/**", "/api/v1/state/**", "/api/v1/products/new-arrivals",
          "/api/v1/products/best-selling", "/api/v1/customer/resendVerificationToken/**"
  };



  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        //USE REQUEST MATCHERS FOR JAVA 17
//         .antMatchers("/api/v1/user/**").permitAll()
            .antMatchers(WHITE_LISTED_URLS)
        .permitAll()
            .antMatchers("/api/v1/user/**").permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
