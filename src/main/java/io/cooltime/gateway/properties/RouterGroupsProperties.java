package io.cooltime.gateway.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "routers")
public class RouterGroupsProperties {
	private Map<String, RouterProperties> defaults;
}