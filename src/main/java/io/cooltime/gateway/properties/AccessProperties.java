package io.cooltime.gateway.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;
import com.nimbusds.jose.util.ArrayUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.access")
public class AccessProperties {
	private List<String> free = Lists.newArrayList();
	private List<String> nologin = Lists.newArrayList();

	public String[] getExcludesArray(String... pattern) {
		return ArrayUtils.concat(free.toArray(new String[0]), nologin.toArray(new String[0]), pattern);
	}
}