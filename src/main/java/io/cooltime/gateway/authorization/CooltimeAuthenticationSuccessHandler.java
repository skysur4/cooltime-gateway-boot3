/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cooltime.gateway.authorization;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.cooltime.gateway.properties.FrontendProperties;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CooltimeAuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

	@Autowired
	private FrontendProperties frontendProperties;

	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        return webFilterExchange.getExchange().getSession().flatMap(session -> {

    		//TODO 도메인 작업 후 다시 시도
//        	URI returnUri = webFilterExchange.getExchange().getRequest().getURI();
        	super.setLocation(frontendProperties.getURI());

            DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
            OidcUserInfo userInfo = user.getUserInfo();
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            log.info("### Saving user info [{}] to session {} ###", userInfo.getEmail(), session.getId());

            session.getAttributes().put("USER", userInfo);
            session.getAttributes().put("AUTHORITY", authorities);

            return super.onAuthenticationSuccess(webFilterExchange, authentication);
        });
	}
}
