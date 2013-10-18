package de.tbosch.tools.googleapps;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.tbosch.tools.googleapps.utils.GoogleAppsContext;

@ContextConfiguration(locations = "/applicationContext-test.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractSpringTest {

	private static ConfigurableApplicationContext staticContext;

	@Autowired
	private ConfigurableApplicationContext context;

	@Before
	public void beforeAbstractSpring() {
		staticContext = context;
		GoogleAppsContext.load(context);
	}

	@AfterClass
	public static void afterAllAbstractSpring() {
		staticContext.close();
	}

}
