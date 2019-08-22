package com.globokas.auth.springauth0demo.cors;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CustomCorsFilter
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@Component
public class CustomCorsFilter extends CorsFilter {

    public CustomCorsFilter(CorsConfig corsConfig) {
        super(configurationSource(corsConfig));
    }

    private static UrlBasedCorsConfigurationSource configurationSource(CorsConfig corsConfig) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(corsConfig.getAllowedOrigins());
        config.setAllowedHeaders(corsConfig.getAllowedHeaders());
//        config.addExposedHeader("X-XSRF-TOKEN");
//        config.addExposedHeader("X-SET-COOKIE");
        config.setExposedHeaders(corsConfig.getExposedHeaders());
        config.setMaxAge(corsConfig.getMaxAge());
        //config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedMethods(corsConfig.getAllowedMethods());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsConfig.getPath(), config);
        return source;
    }
}