package de.tbosch.commons.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;

public class DataloadUtils {

	/** Die Verbindung zur Datenbank */
	private static DataSource localdataSource;

	/** Flag ob qualifizierte Tabellennamen */
	private static boolean enableQualifiedTableNames = false;

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
	public static void executeDbUnitXmlScript(String pfad) throws IOException, SQLException, DatabaseUnitException {

		// Erstelle das DbUnit-TestDatenSet
		IDatabaseTester databaseTester = new DataSourceDatabaseTester(localdataSource);
		FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSet(DataloadUtils.class.getClass().getResourceAsStream(pfad),
				false);
		databaseTester.setDataSet(flatXmlDataSet);

		// Erstelle die Connection
		IDatabaseConnection databaseConnection = new DatabaseConnection(localdataSource.getConnection());
		DatabaseConfig databaseConfig = databaseConnection.getConfig();

		// Prüfe ob qualifizierte Tabellennamen aktiviert werden sollen
		databaseConfig.setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, enableQualifiedTableNames);
		databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

		try {
			// Speichere die Testdaten
			DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, flatXmlDataSet);
		} finally {
			databaseConnection.close();
		}
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
	public static void executeDbUnitXmlScript(String pfad, DatabaseOperation databaseOperation) throws IOException,
			SQLException, DatabaseUnitException {

		// Erstelle das DbUnit-TestDatenSet
		IDatabaseTester databaseTester = new DataSourceDatabaseTester(localdataSource);
		FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSet(DataloadUtils.class.getClass().getResourceAsStream(pfad),
				false);
		databaseTester.setDataSet(flatXmlDataSet);

		// Erstelle die Connection
		IDatabaseConnection databaseConnection = new DatabaseConnection(localdataSource.getConnection());
		DatabaseConfig databaseConfig = databaseConnection.getConfig();

		// Prüfe ob qualifizierte Tabellennamen aktiviert werden sollen
		databaseConfig.setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, enableQualifiedTableNames);
		databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

		try {
			// Speichere die Testdaten
			databaseOperation.execute(databaseConnection, flatXmlDataSet);
		} finally {
			databaseConnection.close();
		}
	}

	/**
	 * Erzeugt ein DTD Dataset zu der übergeben Tabelle.
	 * 
	 * @param tableName
	 * @param sqlSelectTable
	 * @param path
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	public static void createDbUnitDtdFile(String dtdName, String[] paths, String[] tables)
			throws DatabaseUnitException, SQLException, FileNotFoundException, IOException {
		// Erstelle die Connection
		IDatabaseConnection databaseConnection = new DatabaseConnection(localdataSource.getConnection());
		DatabaseConfig databaseConfig = databaseConnection.getConfig();

		// Prüfe ob qualifizierte Tabellennamen aktiviert werden sollen
		databaseConfig.setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, enableQualifiedTableNames);
		databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

		// Erzeuge Dataset
		QueryDataSet dataSet = new QueryDataSet(databaseConnection);
		for (String table : tables) {
			dataSet.addTable(table.toLowerCase());
		}
		for (String path : paths) {
			FlatDtdDataSet.write(dataSet, new FileOutputStream(path + dtdName + ".dtd"));
		}
	}

	/**
	 * Führt dass Sql-Script am übergebenen Pfad aus.
	 * 
	 * @param pfad
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void executeSqlScript(String pfad, String delimiter) throws IOException, SQLException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(DataloadUtils.class.getClass()
				.getResourceAsStream(pfad)));
		executeSqlScript(reader, delimiter);
	}

	/**
	 * Führt dass Sql-Script im übergebenen File aus.
	 * 
	 * @param sqlFile
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void executeSqlScript(File sqlFile, String delimiter) throws IOException, SQLException {
		BufferedReader reader = new BufferedReader(new FileReader(sqlFile));
		executeSqlScript(reader, delimiter);
	}

	/**
	 * Führt das SQL Skript aus, auf das der Reader referenziert.
	 * 
	 * @param reader
	 *            Reader für das SQL Skript
	 * @param delimiter
	 *            Delimiter, um einzelne SQL Statements zu trennen.
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void executeSqlScript(BufferedReader reader, String delimiter) throws IOException, SQLException {
		String zeile = reader.readLine();
		StringBuilder builder = new StringBuilder();
		while (zeile != null) {
			builder.append(zeile);
			builder.append("\n");
			zeile = reader.readLine();
		}
		// Teile das Skript in einzelne SQL-Statements und führe diese als Batch aus
		Connection connection = localdataSource.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String[] sqls = builder.toString().split(delimiter);
			int counter = 0;
			for (int i = 0; i < sqls.length - 1; i++) {
				counter++;
				statement.addBatch(sqls[i]);
				if (counter == 200) {
					System.out.println(".- Committing 200 rows ... at: " + i);
					statement.executeBatch();
					statement.close();
					counter = 0;
					statement = connection.createStatement();
				}
			}
			statement.executeBatch();
		} finally {
			statement.close();
			connection.close();
		}
	}

	/**
	 * Aktiviert das Feature für qualifizierte Tabellennamen.
	 * 
	 * @param enableQualifiedTableNames
	 */
	public static void enableQualifiedTableNames() {
		enableQualifiedTableNames = true;
	}

	// Getter / Setter

	public static DataSource getLocaldataSource() {
		return localdataSource;
	}

	public static void setLocaldataSource(DataSource localdataSource) {
		DataloadUtils.localdataSource = localdataSource;
	}

}
