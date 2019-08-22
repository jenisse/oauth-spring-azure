package com.globokas.auth.springauth0demo.security.filters;

import com.globokas.auth.springauth0demo.security.handlers.VerifyTokenFailureHandler;
import com.globokas.auth.springauth0demo.security.handlers.VerifyTokenSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VerifyTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final VerifyTokenSuccessHandler successHandler;
    private final VerifyTokenFailureHandler failureHandler;

    public VerifyTokenFilter(String defaultFilterProcessesUrl, VerifyTokenSuccessHandler successHandler, VerifyTokenFailureHandler failureHandler) {
        super(defaultFilterProcessesUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
