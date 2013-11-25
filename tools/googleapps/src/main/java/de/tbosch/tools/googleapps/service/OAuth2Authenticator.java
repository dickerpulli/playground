package de.tbosch.tools.googleapps.service;

import com.google.api.client.auth.oauth2.Credential;

public interface OAuth2Authenticator {



	/**
	 * Authorizes the installed application to access user's protected data.
	 * 
	 * @return Authorized Credentials.
	 */
	Credential authorize() throws Exception;

}