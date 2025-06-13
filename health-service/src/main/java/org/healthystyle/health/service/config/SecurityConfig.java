package org.healthystyle.health.service.config;

import java.util.Arrays;
import java.util.Map;

import org.healthystyle.health.service.config.init.InitializationHealthFilter;
import org.healthystyle.health.service.timezone.TimeZoneFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@EnableScheduling
public class SecurityConfig {
	@Autowired
	private InitializationHealthFilter initializationHealthFilter;
	@Autowired
	private TimeZoneFilter timezoneFilter;

	@Bean
	public SecurityFilterChain filterCharin(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						req -> req.requestMatchers("/oauth2/redirect", "/auth/health", "/oauth2/refresh", "/time")
								.permitAll().anyRequest().authenticated())
				.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				.oauth2ResourceServer(r -> r.jwt(Customizer.withDefaults()))
				.addFilterAfter(initializationHealthFilter, BearerTokenAuthenticationFilter.class)
				.addFilterAfter(timezoneFilter, InitializationHealthFilter.class).build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(
				Arrays.asList("http://localhost:3000", "http://localhost:3003", "http://localhost:3010"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.addExposedHeader("Location");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public NimbusJwtDecoder jwtDecoder() {
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri("http://auth-service:3003/oauth2/jwks").build();

		return decoder;
	}

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "dwadw");
		body.add("client_secret", "dwad");
		body.add("redirect_uri", "dwadw");
		body.add("code", "dwadadw");
		System.out.println(body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<Map<String, String>> response = restTemplate.exchange("http://localhost:3003/oauth2dd/tokend",
				HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, String>>() {
				});
		System.out.println(response);

	}
}
