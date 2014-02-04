package de.tbosch.web.shared;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FieldVerifierTest {

	@Test
	public void testIsValidName() throws Exception {
		assertTrue(FieldVerifier.isValidName("name"));
	}

}
