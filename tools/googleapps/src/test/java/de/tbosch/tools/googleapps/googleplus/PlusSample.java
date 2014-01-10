/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.tbosch.tools.googleapps.googleplus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.Link;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.util.ServiceException;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;

import de.tbosch.tools.googleapps.oauth2.OAuth2SaslClientFactory;
import de.tbosch.tools.googleapps.service.impl.OAuth2AuthenticatorImpl.OAuth2Provider;

/**
 * @author Yaniv Inbar
 */
public class PlusSample {

	/**
	 * Be sure to specify the name of your application. If the application name is {@code null} or blank, the
	 * application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "googleapps";

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

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static Plus plus;

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
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport,
				JSON_FACTORY,
				clientSecrets,
				Arrays.asList(PlusScopes.PLUS_ME, "https://www.googleapis.com/auth/userinfo.email",
						"https://mail.google.com/", CalendarScopes.CALENDAR_READONLY, "https://www.google.com/m8/feeds"))
				.setDataStoreFactory(dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	private static void initialize() {
		Security.addProvider(new OAuth2Provider());
		try {
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			// httpTransport = Goo1gleNetHttpTransport.newTrustedTransport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			// authorization
			Credential credential = authorize();
			// set up global Plus instance
			plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
					.build();
			// run commands
			listActivities();
			getActivity();
			getProfile();
			getEmails(credential);
			getCalendar();
			getAllContacts(credential);
			getUserinfo(credential);
			// success!
			return;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);
	}

	private static void getUserinfo(Credential credential) throws IOException {
		System.out.println("-----------------------------------------------------------");
		// Make an authenticated request
		final GenericUrl url = new GenericUrl("https://www.googleapis.com/oauth2/v2/userinfo");
		final HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = request.execute().parseAsString();
		System.out.println(jsonIdentity);
		JsonObjectParser parser = new JsonObjectParser.Builder(JSON_FACTORY).build();
		Map userinfo = parser.parseAndClose(new StringReader(jsonIdentity), HashMap.class);
		System.out.println(userinfo.get("email"));
		System.out.println("-----------------------------------------------------------");
	}

	public static class Userinfo {
		private String id;
		private String email;
		private String verified_email;

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @param email
		 *            the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}

		/**
		 * @return the verified_email
		 */
		public String getVerified_email() {
			return verified_email;
		}

		/**
		 * @param verified_email
		 *            the verified_email to set
		 */
		public void setVerified_email(String verified_email) {
			this.verified_email = verified_email;
		}

	}

	private static void getCalendar() throws Exception {
		Credential credential = authorize();
		Calendar calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, credential).build();
		Events events = calendar.events().list("primary").execute();
		for (Event e : events.getItems()) {
			System.out.println(e.getStart() + e.getSummary());
		}
	}

	private static void getEmails(Credential credential) throws MessagingException {
		initialize();
		IMAPStore imapStore = connectToImap("imap.gmail.com", 993, "dickerpulli@gmail.com",
				credential.getAccessToken(), true);
		System.out.println(imapStore.getFolder("inbox").getMessageCount());
	}

	private static IMAPStore connectToImap(String host, int port, String userEmail, String oauthToken, boolean debug)
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

	/** List the public activities for the authenticated user. */
	private static void listActivities() throws IOException {
		View.header1("Listing My Activities");
		// Fetch the first page of activities
		Plus.Activities.List listActivities = plus.activities().list("me", "public");
		listActivities.setMaxResults(5L);
		// Pro tip: Use partial responses to improve response time considerably
		listActivities.setFields("nextPageToken,items(id,url,object/content)");
		ActivityFeed feed = listActivities.execute();
		// Keep track of the page number in case we're listing activities
		// for a user with thousands of activities. We'll limit ourselves
		// to 5 pages
		int currentPageNumber = 0;
		while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber <= 5) {
			for (Activity activity : feed.getItems()) {
				View.show(activity);
				View.separator();
			}
			// Fetch the next page
			String nextPageToken = feed.getNextPageToken();
			if (nextPageToken == null) {
				break;
			}
			listActivities.setPageToken(nextPageToken);
			View.header2("New page of activities");
			feed = listActivities.execute();
		}
	}

	/** Get an activity for which we already know the ID. */
	private static void getActivity() throws IOException {
		// A known public activity ID
		String activityId = "z12gtjhq3qn2xxl2o224exwiqruvtda0i";
		// We do not need to be authenticated to fetch this activity
		View.header1("Get an explicit public activity by ID");
		Activity activity = plus.activities().get(activityId).execute();
		View.show(activity);
	}

	/** Get the profile for the authenticated user. */
	private static void getProfile() throws IOException {
		View.header1("Get my Google+ profile");
		Person profile = plus.people().get("me").execute();
		View.show(profile);
	}

	private static void getAllContacts(Credential credential) throws ServiceException, IOException {
		ContactsService myService = new ContactsService(APPLICATION_NAME);
		myService.setOAuth2Credentials(credential);
		// Request the feed
		URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
		ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
		// Print the results
		System.out.println(resultFeed.getTitle().getPlainText());
		for (ContactEntry entry : resultFeed.getEntries()) {
			if (entry.hasName()) {
				Name name = entry.getName();
				if (name.hasFullName()) {
					String fullNameToDisplay = name.getFullName().getValue();
					if (name.getFullName().hasYomi()) {
						fullNameToDisplay += " (" + name.getFullName().getYomi() + ")";
					}
					System.out.println("\\\t\\\t" + fullNameToDisplay);
				} else {
					System.out.println("\\\t\\\t (no full name found)");
				}
				if (name.hasNamePrefix()) {
					System.out.println("\\\t\\\t" + name.getNamePrefix().getValue());
				} else {
					System.out.println("\\\t\\\t (no name prefix found)");
				}
				if (name.hasGivenName()) {
					String givenNameToDisplay = name.getGivenName().getValue();
					if (name.getGivenName().hasYomi()) {
						givenNameToDisplay += " (" + name.getGivenName().getYomi() + ")";
					}
					System.out.println("\\\t\\\t" + givenNameToDisplay);
				} else {
					System.out.println("\\\t\\\t (no given name found)");
				}
				if (name.hasAdditionalName()) {
					String additionalNameToDisplay = name.getAdditionalName().getValue();
					if (name.getAdditionalName().hasYomi()) {
						additionalNameToDisplay += " (" + name.getAdditionalName().getYomi() + ")";
					}
					System.out.println("\\\t\\\t" + additionalNameToDisplay);
				} else {
					System.out.println("\\\t\\\t (no additional name found)");
				}
				if (name.hasFamilyName()) {
					String familyNameToDisplay = name.getFamilyName().getValue();
					if (name.getFamilyName().hasYomi()) {
						familyNameToDisplay += " (" + name.getFamilyName().getYomi() + ")";
					}
					System.out.println("\\\t\\\t" + familyNameToDisplay);
				} else {
					System.out.println("\\\t\\\t (no family name found)");
				}
				if (name.hasNameSuffix()) {
					System.out.println("\\\t\\\t" + name.getNameSuffix().getValue());
				} else {
					System.out.println("\\\t\\\t (no name suffix found)");
				}
			} else {
				System.out.println("\t (no name found)");
			}
			System.out.println("Email addresses:");
			for (Email email : entry.getEmailAddresses()) {
				System.out.print(" " + email.getAddress());
				if (email.getRel() != null) {
					System.out.print(" rel:" + email.getRel());
				}
				if (email.getLabel() != null) {
					System.out.print(" label:" + email.getLabel());
				}
				if (email.getPrimary()) {
					System.out.print(" (primary) ");
				}
				System.out.print("\n");
			}
			System.out.println("IM addresses:");
			for (Im im : entry.getImAddresses()) {
				System.out.print(" " + im.getAddress());
				if (im.getLabel() != null) {
					System.out.print(" label:" + im.getLabel());
				}
				if (im.getRel() != null) {
					System.out.print(" rel:" + im.getRel());
				}
				if (im.getProtocol() != null) {
					System.out.print(" protocol:" + im.getProtocol());
				}
				if (im.getPrimary()) {
					System.out.print(" (primary) ");
				}
				System.out.print("\n");
			}
			System.out.println("Groups:");
			for (GroupMembershipInfo group : entry.getGroupMembershipInfos()) {
				String groupHref = group.getHref();
				System.out.println("  Id: " + groupHref);
			}
			System.out.println("Extended Properties:");
			for (ExtendedProperty property : entry.getExtendedProperties()) {
				if (property.getValue() != null) {
					System.out.println("  " + property.getName() + "(value) = " + property.getValue());
				} else if (property.getXmlBlob() != null) {
					System.out.println("  " + property.getName() + "(xmlBlob)= " + property.getXmlBlob().getBlob());
				}
			}
			Link photoLink = entry.getContactPhotoLink();
			String photoLinkHref = photoLink.getHref();
			System.out.println("Photo Link: " + photoLinkHref);
			if (photoLink.getEtag() != null) {
				System.out.println("Contact Photo's ETag: " + photoLink.getEtag());
			}
			System.out.println("Contact's ETag: " + entry.getEtag());
		}
	}

}