package com.globokas.auth.springauth0demo.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globokas.auth.springauth0demo.config.AppProperties;
import com.globokas.auth.springauth0demo.exceptions.BadRequestException;
import com.globokas.auth.springauth0demo.security.helpers.CookieUtils;
import com.globokas.auth.springauth0demo.security.helpers.HttpCookieOAuth2AuthorizationRequestRepository;
import com.globokas.auth.springauth0demo.security.user.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Value("${cookie.redirect.age}")
    private Integer redirectCookieAge;

    private AppProperties appProperties;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final ObjectMapper mapper;


    public OAuth2AuthenticationSuccessHandler(AppProperties appProperties, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, ObjectMapper mapper) {
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }



        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser userContext2 = (DefaultOidcUser) authentication.getPrincipal();

        


            Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        clearAuthenticationAttributes(request, response);


        Cookie cookie=new Cookie("token", "token");
        cookie.setPath("/");

        response.addCookie(cookie);

        Cookie cookie2=new Cookie("refreshToken", "refreshtoken");
        cookie2.setPath("/");

        response.addCookie(cookie);
        response.addCookie(cookie2);

        Cookie cookie3=new Cookie("redirect", "true");
        cookie3.setMaxAge(redirectCookieAge);
        cookie3.setPath("/");
        response.addCookie(cookie3);

        return UriComponentsBuilder.fromUriString(targetUrl)

                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
