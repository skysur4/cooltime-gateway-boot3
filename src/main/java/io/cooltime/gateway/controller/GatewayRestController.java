package io.cooltime.gateway.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.cooltime.gateway.common.model.LoginStatus;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gateway")
public class GatewayRestController {
	@Autowired
	R2dbcEntityTemplate entityTemplate;

	@Autowired
	private boolean isLocal;

	@GetMapping("/session")
	public Mono<LoginStatus> session (
			@Parameter(hidden = true) @AuthenticationPrincipal Authentication authentication) {
		LoginStatus loginInfo = new LoginStatus();

		if(authentication != null) {
			loginInfo.setStatus(authentication.isAuthenticated());
			OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
			loginInfo.setId(oidcUser.getSubject());
			loginInfo.setFirstName(oidcUser.getGivenName());
			loginInfo.setLastName(oidcUser.getFamilyName());
			loginInfo.setEmail(oidcUser.getEmail());

	        if(isLocal) {
	        	loginInfo.setToken(oidcUser.getIdToken().getTokenValue());
	    	}
		}

		return Mono.just(loginInfo);
	}
}