package de.tbosch.tools.googleapps.service.impl;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import de.tbosch.tools.googleapps.AbstractSpringTest;
import de.tbosch.tools.googleapps.service.PreferencesService;

public class PreferencesServiceImplTest extends AbstractSpringTest {

	@Resource(name = "pref")
	private PreferencesService preferencesService;

	@Test
	public void testWriteReadPassword() throws Exception {
		preferencesService.writePassword("12345");
		assertEquals("12345", preferencesService.readPassword());
	}

}
