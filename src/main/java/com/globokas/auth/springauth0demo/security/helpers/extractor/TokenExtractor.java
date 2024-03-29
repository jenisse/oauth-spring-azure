package com.globokas.auth.springauth0demo.security.helpers.extractor;

/**
 * Implementations of this interface should always return raw base-64 encoded
 * representation of JWT Token.
 * 
 * @author vladimir.stankovic
 *
 * Aug 5, 2016
 */
public interface TokenExtractor {
    public String extract(String payload);
}
