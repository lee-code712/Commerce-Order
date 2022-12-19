package com.digital.v3.context;

import static com.digital.v3.utils.HttpConnectionUtils.*;

import org.springframework.stereotype.Component;

import com.digital.v3.schema.Auth;

@Component
public class AuthContext {

	private static final String baseUrlKey = "API-G";
	
	/* person ID 반환 */
	public long getPersonId (String token) throws Exception {
		
		String responseJson = requestGET(baseUrlKey, "/rest/auth/personInfo", token);
		Auth auth = (Auth) jsonToObject(responseJson, Auth.class);
		
		return auth.getPersonId();
	}
	
	/* token 유효 시간 갱신 */
	public void updateValidTime (String token) throws Exception {
		
		requestPOST(baseUrlKey, "/rest/auth/updateValidTime", null, token);
	}
	
	/* token 유효 여부 확인 */
	public boolean tokenVaild (String token) throws Exception {
		
		if (token == null) return false;
		
		String responseJson = requestGET(baseUrlKey, "/rest/auth/tokenValid", token);
		Auth resAuth = (Auth) jsonToObject(responseJson, Auth.class);
		
		return resAuth.isValidToken();
	}
	
	/* token 만료 여부 확인 */
	public boolean expireCheck (String token) throws Exception {
		
		String responseJson = requestGET(baseUrlKey, "/rest/auth/expireCheck", token);
        Auth resAuth = (Auth) jsonToObject(responseJson, Auth.class);
        
        return resAuth.isExpiredToken();
	}
		
}
