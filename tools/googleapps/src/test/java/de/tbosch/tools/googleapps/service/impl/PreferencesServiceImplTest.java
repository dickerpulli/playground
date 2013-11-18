package de.tbosch.tools.googleapps.service.impl;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import de.tbosch.tools.googleapps.AbstractSpringTest;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;

public class PreferencesServiceImplTest extends AbstractSpringTest {

	@Resource(name = "pref")
	private PreferencesService preferencesService;

	@Test
	public void testWriteReadPassword() throws Exception {
		preferencesService.writePref(PrefKey.PASSWORD, "12345");
		assertEquals("12345", preferencesService.readPref(PrefKey.PASSWORD));
	}

}