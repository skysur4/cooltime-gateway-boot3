package io.cooltime.gateway.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

import io.cooltime.gateway.authorization.CooltimeAuthenticationFailureHandler;
import io.cooltime.gateway.authorization.CooltimeAuthenticationSuccessHandler;
import io.cooltime.gateway.authorization.CooltimeAuthorizationRequestResolver;
import io.cooltime.gateway.authorization.CooltimeServerLogoutSuccessHandler;
import io.cooltime.gateway.properties.AccessProperties;
import io.cooltime.gateway.properties.AuthenticationProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Autowired
	CooltimeAuthorizationRequestResolver   	cooltimeAuthorizationRequestResolver;

	@Autowired
	CooltimeAuthenticationSuccessHandler   	cooltimeAuthenticationSuccessHandler;

	@Autowired
	CooltimeAuthenticationFailureHandler	cooltimeAuthenticationFailureHandler;

	@Autowired
	CooltimeServerLogoutSuccessHandler     	cooltimeServerLogoutSuccessHandler;

    @Autowired
    private AccessProperties accessProperties;

    @Autowired
    private AuthenticationProperties authenticationProperties;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
    	configExclude(http);

        http.headers(headers -> {
        	headers.referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.ORIGIN));
        });

        /**
         * 인증 처리 전 필터 추가
         */
        //http.addFilterBefore(필터, SecurityWebFiltersOrder.AUTHENTICATION);

        http.csrf(csrf -> csrf.disable());
        http.formLogin(formLogin -> formLogin.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());

        http.exceptionHandling(exceptionHandling -> {
        	exceptionHandling.authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint(authenticationProperties.getLoginUrl()));	//추후 프런트쪽으로 테스트
        });

        http.oauth2Login(login -> {
        	Customizer.withDefaults();	// 로그인
        	login.authorizationRequestResolver(cooltimeAuthorizationRequestResolver);	// 로그인 처리를 위한 커스텀 URL (/gateway/**)
        	login.authenticationMatcher(new PathPatternParserServerWebExchangeMatcher(authenticationProperties.getAuthenticationUrl()));	//토큰 발급을 위한 커스텀 URL (/gateway/**)
        	login.authenticationSuccessHandler(cooltimeAuthenticationSuccessHandler);	// 인증 성공 후 액션
        	login.authenticationFailureHandler(cooltimeAuthenticationFailureHandler);	// 인증 실패 후 액션
        });

        http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        http.logout(logout -> {
        	logout.requiresLogout(new PathPatternParserServerWebExchangeMatcher(authenticationProperties.getLogoutUrl()));	// 로그아웃 경로 설정
        	logout.logoutSuccessHandler(cooltimeServerLogoutSuccessHandler);	// 로그아웃 성공 후 액션
        });

        // 세션 관리자 설정
        http.securityContextRepository(new WebSessionServerSecurityContextRepository());

        http.authorizeExchange(exchange -> exchange.anyExchange().authenticated());

        /**
         * 인증 처리 후 필터 추가
         */
        //http.addFilterAfter(필터, SecurityWebFiltersOrder.AUTHORIZATION);

        return http.build();
    }

    /**
     * 로그인 없이 접근 가능한 path 설정
     * @param http
     */
    private void configExclude(ServerHttpSecurity http) {
    	String[] excludes = accessProperties.getExcludesArray();
    	log.info("### Security Excluding patterns: " + Arrays.toString(excludes));

    	http.authorizeExchange(exchanges -> {
			exchanges.pathMatchers(excludes).permitAll();

			// POST 요청 시 preflight 허용
			exchanges.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll();
		});
    }

}
