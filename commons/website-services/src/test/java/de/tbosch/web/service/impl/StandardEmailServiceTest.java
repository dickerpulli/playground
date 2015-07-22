package de.tbosch.web.service.impl;

import static org.junit.Assert.assertEquals;

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
		assertEquals("Dies ist mein Text mit Umlautön...",
				((SmtpMessage) smtpServer.getReceivedEmail().next()).getBody());
	}

}
