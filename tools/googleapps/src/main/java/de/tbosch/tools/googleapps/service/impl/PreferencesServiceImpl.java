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

@Service
public class PreferencesServiceImpl implements PreferencesService {

	private Preferences prefs;

	private boolean inTest = false;

	/**
	 * @param inTest the inTest to set
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
		}
		else {
			prefs = Preferences.userNodeForPackage(PreferencesServiceImpl.class);
		}
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.PreferencesService#readPassword()
	 */
	@Override
	public String readPassword() {
		String raw = prefs.get("password", "");
		return StringUtils.stripToEmpty(decrypt(raw));
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.PreferencesService#readUsername()
	 */
	@Override
	public String readUsername() {
		String raw = prefs.get("username", "");
		return StringUtils.stripToEmpty(decrypt(raw));
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.PreferencesService#writePassword(java.lang.String)
	 */
	@Override
	public void writePassword(String pwd) {
		String raw = encrypt(pwd);
		prefs.put("password", raw);
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.PreferencesService#writeUsername(java.lang.String)
	 */
	@Override
	public void writeUsername(String usr) {
		String raw = encrypt(usr);
		prefs.put("username", raw);
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
		}
		catch (Exception e) {
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
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Error while decoding", e);
		}
	}

}
