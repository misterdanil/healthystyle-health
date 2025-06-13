package org.healthystyle.health.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.healthystyle.health.repository.diet.MealRepository;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.util.oauth2.RefreshTokenException;
import org.healthystyle.util.oauth2.TokenService;
import org.healthystyle.util.oauth2.impl.OAuth2TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
	@Autowired
	private TokenService tokenService;
	private Map<String, String> cachedUris = new HashMap<>();
	
	@Autowired
	private MealRepository mealRepository;

//	@GetMapping("/oauth2/redirect")
//	@ResponseBody
//	public String getOAuth2Redirect(@RequestParam String state, @RequestParam String cachedUri,
//			HttpServletResponse response) throws URISyntaxException, IOException {
//		cachedUris.put(state, cachedUri);
//		System.out.println("returning redirect");
//		return "http://localhost:3003/oauth2/authorize?client_id=health&redirect_uri=http://localhost:3001/auth/health&scope=openid"
//				+ "&response_type=code" + "&response_mode=query" + "&nonce=65jhtt4m9r8" + "&state=" + state;
//	}

	@GetMapping("/oauth2/redirect")
//	@ResponseBody
	public void getOAuth2Redirect(@RequestParam String state, @RequestParam String cachedUri,
			HttpServletResponse response) throws URISyntaxException, IOException {
//		cachedUris.put(state, cachedUri);
		response.sendRedirect(
				"http://localhost:3010/authentication/oauth2/authorize?client_id=health&redirect_uri=http://health-service:3001/auth/health&scope=openid"
						+ "&response_type=code" + "&response_mode=query" + "&nonce=65jhtt4m9r8" + "&state=" + state);
//		return "http://localhost:3003/oauth2/authorize?client_id=health&redirect_uri=http://localhost:3001/auth/health&scope=openid"
//				+ "&response_type=code" + "&response_mode=query" + "&nonce=65jhtt4m9r8" + "&state=" + state;
	}

//	@GetMapping("/oauth2/refresh")
//	@ResponseBody
//	public ResponseEntity<?> refreshTokens(@RequestParam("refresh_token") String refreshToken,
//			HttpServletResponse response) throws URISyntaxException, IOException {
//		Map<String, String> resp;
//		try {
//			resp = tokenService.refreshTokens(refreshToken);
//		} catch (RefreshTokenException e) {
//			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
//		}
//		resp.put("expires", resp.get("expires_in"));
//
//		return ResponseEntity.ok(resp);
//	}

	@GetMapping("/oauth2/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response)
			throws URISyntaxException, IOException {
		String refreshToken = getRefreshToken(request);
		if (refreshToken == null) {
			return ResponseEntity.badRequest().build();
		}
		OAuth2TokenResponse tokenResponse;
		try {
			tokenResponse = tokenService.refreshTokens(refreshToken);
		} catch (RefreshTokenException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		addTokenCookies(tokenResponse, response);

		return ResponseEntity.ok().build();
	}

	private String getRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("refresh_token")) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@GetMapping("/auth/health")
	public ResponseEntity<?> getCode(@RequestParam String code, @RequestParam String state,
			HttpServletResponse response) throws URISyntaxException, IOException {
		System.out.println("getting oauth2 tokens");
		OAuth2TokenResponse tokenResponse = tokenService.getTokensResponse(code);

		addTokenCookies(tokenResponse, response);

		return ResponseEntity.ok().build();
	}

	public void addTokenCookies(OAuth2TokenResponse tokenResponse, HttpServletResponse response) {
		Cookie accessTokenCookie = new Cookie("access_token", tokenResponse.getAccessToken());
		accessTokenCookie.setMaxAge(Integer.valueOf(tokenResponse.getExpiresIn()));
		accessTokenCookie.setPath("/");

		Cookie refreshTokenCookie = new Cookie("refresh_token", tokenResponse.getRefreshToken());
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setPath("/");

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);
	}

	@GetMapping("/time")
	public LocalDateTime getCurrentTime() {
		return mealRepository.getCurrentTime();
	}

	public static void main(String[] args)
			throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
		URL url = new URL("http://treat-service:3001/auth/health");
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
				url.getQuery(), url.getRef());
		System.out.println(uri.toASCIIString());
		System.out.println(URLEncoder.encode(
				"http://localhost:3010/authentication/oauth2/authorize?client_id=health&redirect_uri=http://authservice:3001/auth/health&scope=openid"
						+ "&response_type=code" + "&response_mode=query" + "&nonce=65jhtt4m9r8" + "&state="
						+ "dwadasd5648d",
				"UTF-8"));
	}
}
