package com.digital.v3.context;

import org.springframework.stereotype.Component;

import static com.digital.v3.utils.HttpConnectionUtils.*;

@Component
public class PersonContext {
	
	private static final String baseUrlKey = "API-G";

	/* 로그아웃 */
	public String logout (String token) throws Exception {

		return requestGET(baseUrlKey, "/rest/person/logout", token);
	}
}
