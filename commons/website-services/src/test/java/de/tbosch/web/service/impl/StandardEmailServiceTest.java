package de.tbosch.web.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;

import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import de.tbosch.web.test.TestConfiguration;

@ContextConfiguration(classes = { TestConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class StandardEmailServiceTest {

	private static SimpleSmtpServer smtpServer;

	@Autowired
	private StandardEmailService service;

	@BeforeClass
	public static void beforeAll() {
		smtpServer = SimpleSmtpServer.start(9999);
	}

	@AfterClass
	public static void afterAll() {
		smtpServer.stop();
	}

	@Test
	public void sendMeAnEmail() throws Exception {
		boolean sent = service.sendMeAnEmail("Max Mustermann", "Test", "max.mustermann@mail.com",
				"Dies ist mein Text mit Umlautön...");
		assertEquals(true, sent);
		assertEquals(1, smtpServer.getReceivedEmailSize());
		SmtpMessage message = (SmtpMessage) smtpServer.getReceivedEmail().next();
		String body = new String(QuotedPrintableCodec.decodeQuotedPrintable(message.getBody().getBytes()),
				Charset.forName("UTF-8"));
		assertTrue(body.contains("Von Max Mustermann"));
		assertTrue(body.contains("(max.mustermann@mail.com)"));
		assertTrue(body.contains("Dies ist mein Text mit Umlautön..."));
	}

}
