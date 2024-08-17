package io.cooltime.gateway.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.cooltime.gateway.properties.AuthenticationProperties;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/gateway")
public class GatewayController {
	@Autowired
	R2dbcEntityTemplate entityTemplate;

	@Autowired
	AuthenticationProperties authenticationProperties;

	/**
	 * 키클락 로그인
	 * @return
	 */
	@GetMapping("/login")
	public Mono<Void> login (
			@Parameter(hidden = true) ServerWebExchange serverWebExchange,
			@Parameter(hidden = true) ServerHttpResponse response) {

		//TODO 도메인 작업 후 다시 시도
//		String returnUri = serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.REFERER);

		String registrationId = "keycloak";
		UriComponents uriComponents = UriComponentsBuilder
				.fromUriString(authenticationProperties.getAuthorizationUrl()).buildAndExpand(registrationId);

		response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
		response.getHeaders().setLocation(uriComponents.toUri());

		return response.setComplete();

	}
}