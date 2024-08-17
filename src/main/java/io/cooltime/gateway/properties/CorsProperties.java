package io.cooltime.gateway.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {
	private List<String> allowedOrigin;
}