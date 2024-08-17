package io.cooltime.gateway;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class GatewayApplication {
	public static final String CONTEXT = "/gateway";

	public static void main(String[] args) {
		System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
		try {
			System.setProperty("hostname", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			log.error("!!! Fails to get hostname: {}", e.getCause());
			System.setProperty("hostname", "");
		}

		ApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);
		checkProperties(applicationContext.getEnvironment());
	}

	private static void checkProperties(Environment env){
		log.info("BASE-PATH: {}", env.getProperty("spring.webflux.base-path"));
	}
}
