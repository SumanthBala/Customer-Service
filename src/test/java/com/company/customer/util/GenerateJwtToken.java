package com.company.customer.util;

import java.util.Date;
import java.util.HashMap;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class GenerateJwtToken {

	public static String generateMockJwtToken() {
		// Set expiration to 1 hour from now
		long now = System.currentTimeMillis();
		long expirationMillis = now + 3600000; // 1 hour

		return Jwts.builder().setId("0oaglbwmooH0iReAG5d7").setSubject("0oaglbwmooH0iReAG5d7") // Set subject (user ID
																								// or username)
				.setExpiration(new Date(expirationMillis))
				.signWith(SignatureAlgorithm.HS512, "mrksS0lud5a7QxzUWoPCbBsg-pRde2p5FplerE9MZryxxQDbTW02Vb6R8WNXi-WD") // Sign
				// with
				// your
				// secret
				// key
				.compact();
	}

	public static String generateToken() throws OAuthSystemException, OAuthProblemException {
		String URL = "https://dev-19327973.okta.com/oauth2/default/v1/token";

		OAuthClient client = new OAuthClient(new URLConnectionClient());
		OAuthClientRequest request = OAuthClientRequest.tokenLocation(URL).setGrantType(GrantType.CLIENT_CREDENTIALS)
				.setClientId("0oaglbwmooH0iReAG5d7")
				.setClientSecret("mrksS0lud5a7QxzUWoPCbBsg-pRde2p5FplerE9MZryxxQDbTW02Vb6R8WNXi-WD")
				.setScope("test-scope").buildBodyMessage();

		String token = client.accessToken(request, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class)
				.getAccessToken();
		return token;
	}

}
