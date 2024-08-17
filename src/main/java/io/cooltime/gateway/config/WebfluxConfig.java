package io.cooltime.gateway.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.cooltime.gateway.properties.CorsProperties;
import io.cooltime.gateway.properties.FrontendProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebfluxConfig implements WebFluxConfigurer {

	@Autowired
	private CorsProperties corsProperties;

	@Autowired
	private FrontendProperties frontendProperties;

    @Autowired
    private Environment env;

	@Bean
    CorsWebFilter corsWebFilter() {
    	CorsConfiguration corsConfiguration = new CorsConfiguration();

    	corsConfiguration.setAllowCredentials(true);	//서브도메인 세션 공유
    	corsConfiguration.addAllowedMethod("*");
    	corsConfiguration.addAllowedHeader("*");
    	corsConfiguration.setAllowedOrigins(corsProperties.getAllowedOrigin());
    	corsConfiguration.setMaxAge(3600L);

    	UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    	urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    	log.info("### test {}", frontendProperties.getUrl());
    	log.info("### CORS allowed to {}", corsProperties.getAllowedOrigin());

    	return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return objectMapper;
	}

    @Bean
    Boolean isLocal() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        return profiles.contains("local");
    }
}
