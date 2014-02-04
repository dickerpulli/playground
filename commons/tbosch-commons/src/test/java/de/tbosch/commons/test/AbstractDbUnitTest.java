package de.tbosch.commons.test;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import de.tbosch.commons.test.util.DataloadUtils;

/**
 * Abstrakte Oberklasse für Testfälle und Test-Unterklasse, welche DbUnit-Funktionalitäten benötigen. Die Klasse bietet
 * zusätzliche Funktionalitäten für die Arbeit mit Sql.
 */
@TestExecutionListeners( { TransactionalTestExecutionListener.class })
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractDbUnitTest extends AbstractJUnit4SpringContextTests {

	/** Die Verbindung zur Datenbank */
	@Resource
	private DataSource dataSource;

	/** Flag ob qualifizierte Tabellennamen */
	private boolean enableQualifiedTableNames = false;

	/**
	 * Nimmt den Pfad der Xml-Datei mit den Testdaten entgegen und speichert diese in der Datenbank. Die Xml-Datei muss
	 * der DbUnit-Spezifikation entsprechen. Vor der Ausführung des Inserts wird ein "DELETE ALL" auf der Tabelle
	 * ausgeführt. Dies entspricht in DbUnit einem CLEAN_INSERT-Kommando.
	 * 
	 * @param pfad
	 *            wo Xml-Datei mit den Testdaten liegt
	 * @throws IOException
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	public void executeDbUnitXmlScript(String pfad) throws IOException, SQLException, DatabaseUnitException {

		DataloadUtils.setLocaldataSource(dataSource);
		if (enableQualifiedTableNames)
			DataloadUtils.enableQualifiedTableNames();
		DataloadUtils.executeDbUnitXmlScript(pfad);

	}

	/**
	 * Nimmt den Pfad der Xml-Datei mit den Testdaten entgegen und speichert diese in der Datenbank. Die Xml-Datei muss
	 * der DbUnit-Spezifikation entsprechen.
	 * 
	 * @param pfad
	 *            wo Xml-Datei mit den Testdaten liegt
	 * @param databaseOperation
	 *            Die Operation für das "execute", z.B. ein CLEAN_INSERT führt zu einem "DELETE ALL" auf der Tabelle
	 * @throws IOException
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	public void executeDbUnitXmlScript(String pfad, DatabaseOperation databaseOperation) throws IOException,
			SQLException, DatabaseUnitException {

		DataloadUtils.setLocaldataSource(dataSource);
		if (enableQualifiedTableNames)
			DataloadUtils.enableQualifiedTableNames();
		DataloadUtils.executeDbUnitXmlScript(pfad, databaseOperation);

	}

	/**
	 * F�hrt dass Sql-Script am übergebenen Pfad aus.
	 * 
	 * @param pfad
	 * @throws IOException
	 * @throws SQLException
	 */
	public void executeSqlScript(String pfad, String delimiter) throws IOException, SQLException {

		DataloadUtils.setLocaldataSource(dataSource);
		if (enableQualifiedTableNames)
			DataloadUtils.enableQualifiedTableNames();
		DataloadUtils.executeSqlScript(pfad, delimiter);

	}

	/**
	 * Aktiviert das Feature für qualifizierte Tabellennamen.
	 * 
	 * @param enableQualifiedTableNames
	 */
	public void enableQualifiedTableNames() {
		this.enableQualifiedTableNames = true;
	}

	// Getter / Setter

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
