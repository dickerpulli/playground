package de.tbosch.tools.googleapps.service;

/**
 * Reads and writes Preferences from User-Preferences file.
 * 
 * @author Thomas Bosch
 */
public interface PreferencesService {

	/**
	 * The key of the preference in the file.
	 */
	public enum PrefKey {

		PASSWORD(1, true),

		USERNAME(2, true),

		AUTOCONNECT(3, false);

		private final boolean encrypted;

		private final int id;

		private PrefKey(int id, boolean encrypted) {
			this.id = id;
			this.encrypted = encrypted;
		}

		public boolean isEncrypted() {
			return encrypted;
		}

		public int getId() {
			return id;
		}
	}

	/**
	 * Read preference from user preferences.
	 * 
	 * @param key
	 *            PrefKey.
	 * @param key
	 *            PrefKey.
	 * @return Pref.
	 */
	public String readPref(PrefKey key);

	/**
	 * Write preference to user preferences.
	 * 
	 * @param key
	 *            PrefKey.
	 * @param pref
	 *            Pref.
	 * @param key
	 *            PrefKey.
	 * @param pref
	 *            Pref.
	 */
	public void writePref(PrefKey key, String pref);

}