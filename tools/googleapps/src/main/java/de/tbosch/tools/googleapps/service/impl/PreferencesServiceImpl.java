package de.tbosch.tools.googleapps.service.impl;

import java.util.prefs.Preferences;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.tbosch.tools.googleapps.service.PreferencesService;

/**
 * Default-Implementation of {@link PreferencesService}.
<<<<<<< HEAD
 * 
=======
>>>>>>> branch 'master' of https://github.com/dickerpulli/playground.git
 * @author Thomas Bosch
 */
@Service
public class PreferencesServiceImpl implements PreferencesService {

	private Preferences prefs;

	private boolean inTest = false;

	/**
	 * @param inTest
	 *            the inTest to set
	 */
	public void setInTest(boolean inTest) {
		this.inTest = inTest;
	}

	/**
	 * Prefs create.
	 */
	@PostConstruct
	public void createPrefs() {
		if (inTest) {
			prefs = Preferences.userNodeForPackage(Object.class);
		} else {
			prefs = Preferences.userNodeForPackage(PreferencesServiceImpl.class);
		}
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.PreferencesService#readPref(de.tbosch.tools.googleapps.service.PreferencesService.PrefKey)
	 */
	@Override
	public String readPref(PrefKey key) {
		String value = prefs.get(Integer.toString(key.getId()), "");
		if (key.isEncrypted()) {
			value = StringUtils.stripToEmpty(decrypt(value));
		}
		return value;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.PreferencesService#writePref(de.tbosch.tools.googleapps.service.PreferencesService.PrefKey,
	 *      java.lang.String)
	 */
	@Override
	public void writePref(PrefKey key, String pref) {
		String value = pref;
		if (key.isEncrypted()) {
			value = encrypt(value);
		}
		prefs.put(Integer.toString(key.getId()), value);
	}

	@SuppressWarnings("restriction")
	private String decrypt(String encrypted) {
		try {
			DESKeySpec dks = new DESKeySpec("internerkey".getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			sun.misc.BASE64Decoder encoder = new sun.misc.BASE64Decoder();
			byte[] raw = encoder.decodeBuffer(encrypted);
			byte[] stringBytes = cipher.doFinal(raw);
			return new String(stringBytes, "UTF8");
		} catch (Exception e) {
			throw new IllegalArgumentException("Error while decoding", e);
		}
	}

	@SuppressWarnings("restriction")
	private String encrypt(String decrypted) {
		try {
			DESKeySpec dks = new DESKeySpec("internerkey".getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] raw = cipher.doFinal(decrypted.getBytes("UTF8"));
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			return encoder.encode(raw);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error while decoding", e);
		}
	}

}
