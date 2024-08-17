package io.cooltime.gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.cooltime.gateway.properties.SwaggerProperties;

@Profile("!prod")
@Configuration
public class SwaggerConfig {
	@Autowired
	SwaggerProperties swaggerProperties;

	  @Bean
	  GroupedOpenApi publicApi() {
	      return GroupedOpenApi.builder()
	              .group("public")
	              .pathsToMatch("/public/**")
	              .addOpenApiCustomizer(openApi -> openApi.info(swaggerProperties.getInfo()))
	              .build();
	  }

	  @Bean
	  GroupedOpenApi gatewayApi() {
	      return GroupedOpenApi.builder()
	              .group("gateway")
	              .pathsToMatch("/gateway/**")
	              .addOpenApiCustomizer(openApi -> openApi.info(swaggerProperties.getInfo()))
	              .build();
	  }

	  @Bean
	  GroupedOpenApi adminApi() {
	      return GroupedOpenApi.builder()
	              .group("admin")
	              .pathsToMatch("/admin/**")
	              .addOpenApiCustomizer(openApi -> openApi.info(swaggerProperties.getInfo()))
	              .build();
	  }
}
