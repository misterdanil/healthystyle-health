package org.healthystyle.health.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.healthystyle.util.oauth2.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
	@Autowired
	private TokenService tokenService;
	private Map<String, String> cachedUris = new HashMap<>();

	@GetMapping("/oauth2/redirect")
	@ResponseBody
	public String getOAuth2Redirect(@RequestParam String state, @RequestParam String cachedUri,
			HttpServletResponse response) throws URISyntaxException, IOException {
		cachedUris.put(state, cachedUri);
		return "http://localhost:3003/oauth2/authorize?client_id=health&redirect_uri=http://localhost:3001/auth/health&scope=openid"
				+ "&response_type=code" + "&response_mode=query" + "&nonce=65jhtt4m9r8" + "&state=" + state;
	}

	@GetMapping("/auth/health")
	@ResponseStatus(code = HttpStatus.OK)
	public Map<String, String> getCode(@RequestParam String code, @RequestParam String state,
			HttpServletResponse response) throws URISyntaxException, IOException {
		Map<String, String> resp = tokenService.getTokens(code);
		resp.put("cached_uri", cachedUris.get(state));
		return resp;
	}
}
