/* Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tbosch.tools.googleapps.oauth2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.google.api.services.plus.PlusScopes;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.smtp.SMTPTransport;

import de.tbosch.tools.googleapps.googleplus.PlusSample;

/**
 * Performs OAuth2 authentication.
 * 
 * <p>
 * Before using this class, you must call {@code initialize} to install the OAuth2 SASL provider.
 */
public class OAuth2Authenticator {

	private static final Log LOG = LogFactory.getLog(OAuth2Authenticator.class);

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/plus_sample");

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
			put("SaslClientFactory.XOAUTH2", "de.tbosch.tools.googleapps.oauth2.OAuth2SaslClientFactory");
		}
	}

	/**
	 * Installs the OAuth2 SASL provider. This must be called exactly once before calling other methods on this class.
	 */
	public static void initialize() {
		Security.addProvider(new OAuth2Provider());
		try {
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			// httpTransport = Goo1gleNetHttpTransport.newTrustedTransport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Connects and authenticates to an IMAP server with OAuth2. You must have called {@code initialize}.
	 * 
	 * @param host
	 *            Hostname of the imap server, for example {@code imap.googlemail.com}.
	 * @param port
	 *            Port of the imap server, for example 993.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example {@code oauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param debug
	 *            Whether to enable debug logging on the IMAP connection.
	 * 
	 * @return An authenticated IMAPStore that can be used for IMAP operations.
	 */
	public static IMAPStore connectToImap(String host, int port, String userEmail, String oauthToken, boolean debug)
			throws MessagingException {
		Properties props = new Properties();
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		IMAPSSLStore store = new IMAPSSLStore(session, unusedUrlName);
		final String emptyPassword = "";
		store.connect(host, port, userEmail, emptyPassword);
		return store;
	}

	/**
	 * Connects and authenticates to an SMTP server with OAuth2. You must have called {@code initialize}.
	 * 
	 * @param host
	 *            Hostname of the smtp server, for example {@code smtp.googlemail.com}.
	 * @param port
	 *            Port of the smtp server, for example 587.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example {@code oauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param debug
	 *            Whether to enable debug logging on the connection.
	 * 
	 * @return An authenticated SMTPTransport that can be used for SMTP operations.
	 */
	public static SMTPTransport connectToSmtp(String host, int port, String userEmail, String oauthToken, boolean debug)
			throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.sasl.enable", "true");
		props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		SMTPTransport transport = new SMTPTransport(session, unusedUrlName);
		// If the password is non-null, SMTP tries to do AUTH LOGIN.
		final String emptyPassword = "";
		transport.connect(host, port, userEmail, emptyPassword);

		return transport;
	}

	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorize() throws Exception {
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
				clientSecrets, Arrays.asList(PlusScopes.PLUS_ME, "https://mail.google.com/")).setDataStoreFactory(
				dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	/**
	 * Authenticates to IMAP with parameters passed in on the commandline.
	 */
	public static void main(String args[]) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: OAuth2Authenticator <email> <oauthToken>");
			return;
		}
		String email = args[0];
		String oauthToken = args[1];

		initialize();

		IMAPStore imapStore = connectToImap("imap.gmail.com", 993, email, oauthToken, true);
		System.out.println("Successfully authenticated to IMAP.\n");
		SMTPTransport smtpTransport = connectToSmtp("smtp.gmail.com", 587, email, oauthToken, true);
		System.out.println("Successfully authenticated to SMTP.");
	}
}