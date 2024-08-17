package io.cooltime.gateway.filters.routing;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import io.cooltime.gateway.GatewayConsts;

@Component
public class JwtRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtRelayGatewayFilterFactory.Config> {
	private List<String> authorities = Lists.newArrayList();

	@Autowired
	ObjectMapper objectMapper;

	public static class Config {
	}

	public GatewayFilter apply() {
		Config c = new Config();
		return apply(c);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> exchange.getPrincipal()
				.log("token-relay-filter")
				.filter(principal -> principal instanceof Authentication)
				.cast(Authentication.class)
				.filter(authentication -> authentication.isAuthenticated())
				.map(authentication -> setBearerAuth(exchange, authentication))
				// TODO: adjustable behavior if empty
				.defaultIfEmpty(exchange).flatMap(chain::filter);
	}

	private ServerWebExchange setBearerAuth(ServerWebExchange exchange, Authentication authentication) {
		OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

		Map<String, List<String>> realmAccess = objectMapper.convertValue(oidcUser.getAttribute("realm_access"), new TypeReference<Map<String, List<String>>>(){});

		authorities.add(GatewayConsts.FREE_USER);

		if(realmAccess.containsKey("roles")) {
			List<String> roles = realmAccess.get("roles");
			if(roles.contains(GatewayConsts.ADMIN)) {
				authorities.add(GatewayConsts.ADMIN);
			}

			if(roles.contains(GatewayConsts.PAID_USER)) {
				authorities.add(GatewayConsts.PAID_USER);
			}
		}

		ServerHttpRequest request = exchange
				.getRequest()
				.mutate()
				.headers(headers -> {
			    	headers.setBearerAuth(oidcUser.getIdToken().getTokenValue());
					headers.addAll(GatewayConsts.X_AUTHORITY_HEADER, authorities);
			     }).build();

		return exchange.mutate().request(request).build();

//		return exchange.mutate().request(r -> r.headers(headers -> {
//			headers.setBearerAuth(oidcUser.getIdToken().getTokenValue());
//			headers.addAll(GatewayConsts.X_AUTHORITY_HEADER, authorities);
//		})).build();
	}


}

