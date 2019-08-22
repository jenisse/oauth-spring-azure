package com.globokas.auth.springauth0demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globokas.auth.springauth0demo.cors.CustomCorsFilter;
import com.globokas.auth.springauth0demo.security.filters.VerifyTokenFilter;
import com.globokas.auth.springauth0demo.security.handlers.*;
import com.globokas.auth.springauth0demo.security.helpers.HttpCookieOAuth2AuthorizationRequestRepository;
import com.globokas.auth.springauth0demo.security.helpers.RestAuthenticationEntryPoint;
import com.globokas.auth.springauth0demo.security.helpers.extractor.TokenExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {


    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String GOOGLE_AUTHENTICATION_URL = "/api/google/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String API_ROOT_URL = "/api/**";
    public static final String TOKEN_BASED_EXCLUDED_PATH="/api/excluded/**";
    public static final String AJAX_BASED_LOGOUT_ENTRY_POINT = "/api/auth/logout";
    public static final String API_KEY_BASED_PATH= "/api/service/**";

    private CustomCorsFilter corsFilter;

    private RestAuthenticationEntryPoint authenticationEntryPoint;


    private TokenExtractor tokenExtractor;

    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper;



    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;


    private CustomLogoutSuccessHandler logoutSuccessHandler;




    private List<String> permitAllEndpointList2 = Arrays.asList(
            AUTHENTICATION_URL,
            REFRESH_TOKEN_URL,
            AJAX_BASED_LOGOUT_ENTRY_POINT,
            GOOGLE_AUTHENTICATION_URL,
            TOKEN_BASED_EXCLUDED_PATH,
            API_KEY_BASED_PATH,
            "/console",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/**",
            "/webjars/**",
            "/api-docs/**"
    );




    public WebSecurityConfig(CustomCorsFilter corsFilter,
                             RestAuthenticationEntryPoint authenticationEntryPoint,
                             TokenExtractor tokenExtractor,
                             ObjectMapper objectMapper,
                             OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                             OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                             CustomLogoutSuccessHandler logoutSuccessHandler) {
        this.corsFilter = corsFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;

        this.tokenExtractor = tokenExtractor;
        this.objectMapper = objectMapper;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;

    }






    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // We don't need CSRF for JWT based authentication
                .exceptionHandling()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList2.toArray(new String[permitAllEndpointList2.size()]))
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(API_ROOT_URL).authenticated() // Protected API End-points
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/api/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)

                .and()
                .addFilterBefore(this.corsFilter, CorsFilter.class)
                .logout()
                .logoutUrl(AJAX_BASED_LOGOUT_ENTRY_POINT)
                .logoutSuccessHandler(logoutSuccessHandler);


    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }


}
