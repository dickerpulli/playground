package de.tbosch.tools.googleapps.service.impl;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.sun.javafx.application.PlatformImpl;

import de.tbosch.tools.googleapps.controller.AuthorizeController;
import de.tbosch.tools.googleapps.oauth2.OAuth2SaslClientFactory;
import de.tbosch.tools.googleapps.service.OAuth2Authenticator;
import de.tbosch.tools.googleapps.utils.GoogleAppsContext;
import de.tbosch.tools.googleapps.utils.MessageHelper;

/**
 * Performs OAuth2 authentication.
 */
@Service
public class OAuth2AuthenticatorImpl implements OAuth2Authenticator {

	private static final Log LOG = LogFactory.getLog(OAuth2AuthenticatorImpl.class);

	@Autowired
	private ApplicationContext context;

	/** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/googleapps");

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single globally shared
	 * instance across your application.
	 */
	private FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private HttpTransport httpTransport;

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
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			// httpTransport = Goo1gleNetHttpTransport.newTrustedTransport();
		} catch (Exception e) {
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
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(this
				.getClass().getResourceAsStream("/client_secrets.json")));

		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Arrays.asList("https://mail.google.com/", "https://www.googleapis.com/auth/calendar"))
				.setDataStoreFactory(dataStoreFactory).build();

		// authorize
		return new MyAuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	/**
	 * JavaFX compatible flow.
	 */
	private class MyAuthorizationCodeInstalledApp extends AuthorizationCodeInstalledApp {

		private Stage stage;

		public MyAuthorizationCodeInstalledApp(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
			super(flow, receiver);
		}

		/**
		 * @see com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp#onAuthorization(com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl)
		 */
		@Override
		protected void onAuthorization(final AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
			if (Desktop.isDesktopSupported()) {
				super.onAuthorization(authorizationUrl);
			} else {
				PlatformImpl.startup(new Runnable() {

					@Override
					public void run() {
						try {
							AuthorizeController controller = context.getBean(AuthorizeController.class);
							controller.setUrl(authorizationUrl.build());
							Parent parent = GoogleAppsContext.getSpringFXMLLoader().load("../fxml/Authorize.fxml",
									AuthorizeController.class);
							Scene scene = new Scene(parent);
							stage = new Stage();
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.setResizable(false);
							stage.setFullScreen(false);
							stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
								@Override
								public void handle(WindowEvent arg0) {
									arg0.consume();
								}
							});
							stage.setTitle(MessageHelper.getMessage("authorize.title"));
							stage.setScene(scene);
							stage.show();
						} catch (IOException e) {
							throw new IllegalArgumentException("Failed to open scene", e);
						}
					}
				});
			}
		}

		/**
		 * @see com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp#authorize(java.lang.String)
		 */
		@Override
		public Credential authorize(String userId) throws IOException {
			// Dispose informational window after athorization
			Credential credential = super.authorize(userId);
			if (stage != null && stage.isShowing()) {
				PlatformImpl.startup(new Runnable() {

					@Override
					public void run() {
						stage.hide();
					}
				});
			}
			return credential;
		}
	}

}