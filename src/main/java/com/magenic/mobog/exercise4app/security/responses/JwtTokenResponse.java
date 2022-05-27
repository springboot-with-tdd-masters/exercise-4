package com.magenic.mobog.exercise4app.security.responses;


public class JwtTokenResponse {

	private String token;

	public JwtTokenResponse(String jwtToken) {
		this.token = jwtToken;
	}

	public String getToken() {
		return token;
	}
	
}