package de.tbosch.tools.googleapps.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import de.tbosch.tools.googleapps.googleplus.PlusSample;
import de.tbosch.tools.googleapps.oauth2.OAuth2SaslClientFactory;
import de.tbosch.tools.googleapps.service.OAuth2Authenticator;

/**
 * Performs OAuth2 authentication.
 * 
 * <p>
 * Before using this class, you must call {@code initialize} to install the OAuth2 SASL provider.
 */
@Service
public class OAuth2AuthenticatorImpl implements OAuth2Authenticator {

	private static final Log LOG = LogFactory.getLog(OAuth2AuthenticatorImpl.class);

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/googleapps");

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single globally shared
	 * instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	public static final class OAuth2Provider extends Provider {
		private static final long serialVersionUID = 1L;

		public OAuth2Provider() {
			super("Google OAuth2 Provider", 1.0, "Provides the XOAUTH2 SASL Mechanism");
			put("SaslClientFactory.XOAUTH2", OAuth2SaslClientFactory.class.getName());
		}
	}

	/**
	 * Installs the OAuth2 SASL provider. This must be called exactly once before calling other methods on this class.
	 */
	@PostConstruct
	public void initialize() {
		Security.addProvider(new OAuth2Provider());
		try {
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			// httpTransport = Goo1gleNetHttpTransport.newTrustedTransport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (LOG.isErrorEnabled()) {
				LOG.error("data store cannot be opened", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Credential authorize() throws Exception {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(
				PlusSample.class.getResourceAsStream("/client_secrets.json")));
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=plus "
					+ "into plus-cmdline-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}

		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Arrays.asList("https://mail.google.com/", "https://www.googleapis.com/auth/calendar"))
				.setDataStoreFactory(dataStoreFactory).build();

		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

}