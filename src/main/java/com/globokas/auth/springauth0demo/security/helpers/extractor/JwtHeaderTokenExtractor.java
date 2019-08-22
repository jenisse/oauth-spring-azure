package com.globokas.auth.springauth0demo.security.helpers.extractor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link TokenExtractor} extracts token from
 * Authorization: Bearer scheme.
 * 
 * @author vladimir.stankovic
 *
 * Aug 5, 2016
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
    public static String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Cabecera de autorizacion no puede  estar vacia");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Token debe ser del tipo Bearer");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}
