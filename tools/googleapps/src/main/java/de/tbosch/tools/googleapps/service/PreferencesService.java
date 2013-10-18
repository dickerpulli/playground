package de.tbosch.tools.googleapps.service;

public interface PreferencesService {

	/**
	 * Read password from user preferences.
	 * @return pwd
	 */
	public String readPassword();

	/**
	 * Read username from user preferences.
	 * @return usr
	 */
	public String readUsername();

	/**
	 * Write password to user preferences.
	 * @param pwd Password.
	 */
	public void writePassword(String pwd);

	/**
	 * Write username to user preferences.
	 * @param usr Username.
	 */
	public void writeUsername(String usr);

}
