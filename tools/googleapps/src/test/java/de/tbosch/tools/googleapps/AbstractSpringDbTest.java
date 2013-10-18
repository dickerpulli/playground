package de.tbosch.tools.googleapps;

import java.io.IOException;

import javax.sql.DataSource;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

public abstract class AbstractSpringDbTest extends AbstractSpringTest {

	@Autowired
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Before
	public void beforeAbstractDb() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void executeSql(String filename) throws IOException {
		JdbcTestUtils.executeSqlScript(jdbcTemplate, new ClassPathResource(filename), false);
	}

}
