package io.cooltime.gateway.mapper;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.cooltime.gateway.properties.RouterProperties;

import lombok.Setter;

@Setter
@ConfigurationProperties(prefix = "routers")
public class RouterMapper {
	
	private Map<String, RouterProperties> defaults;
	
	public Map<String, RouterProperties> list() {
		return this.defaults;
	}
	
	public RouterProperties findById(String id) {
		return this.defaults.get(id);
	}
	
	public RouterProperties findByContext(String context) {
		return this.defaults.values().stream().filter(r -> context.startsWith(r.getContext())).findFirst().get();
	}
}
