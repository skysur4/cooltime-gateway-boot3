package io.cooltime.gateway.properties;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "application.authentication")
public class AuthenticationProperties {
	private String authorizationUrl;
	private String authenticationUrl;

	private String loginUrl;
	private String logoutUrl;

	private URI getURI(String path) {
		URI uri = null;
		try {
			uri = new URI(path);
		} catch (URISyntaxException | IllegalArgumentException e) {
			log.error("!!! Error while building uri: {}", e.getMessage());
		}

		return uri;
	}

    public URI getAuthenticationURI() {
    	return getURI(authenticationUrl);
    }

    public URI getAuthorizationURI() {
    	return getURI(authorizationUrl);
    }


    public URI getLoginURI() {
    	return getURI(loginUrl);
    }

    public URI getLogoutURI() {
    	return getURI(logoutUrl);
    }
}