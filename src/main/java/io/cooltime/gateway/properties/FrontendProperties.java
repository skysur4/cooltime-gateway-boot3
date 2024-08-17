package io.cooltime.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.frontend")
public class FrontendProperties extends CommonUrlProperties {
}