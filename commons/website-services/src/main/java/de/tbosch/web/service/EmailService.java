package de.tbosch.web.service;

/**
 * Service für Emails.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface EmailService {

	/**
	 * Sende mir eine Email.
	 * 
	 * @param name
	 *            Der Name des Versenders.
	 * @param subject
	 *            Der Betreff.
	 * @param email
	 *            Die Adresse des Versenders.
	 * @param message
	 *            Der Text.
	 * @return Erfolgreich versendet?
	 */
	boolean sendMeAnEmail(String name, String subject, String email, String message);

}
