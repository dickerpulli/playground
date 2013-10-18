package de.tbosch.commons.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.tbosch.commons.persistence.dao.GenericDao;

/**
 * Abstrakte Testklasse für das generische Dao. Diese Testklasse prüft für alle Dao's die
 * Standard-Crud-Funktionalitäten.
 * 
 * In der ableitenden Klasse muss über @see <code>getCreateTestInstanz</code> eine Testinstanz der spezifischen Klasse
 * zurückgegeben werden. Weiterhin muss zum Ausführen der Testfällle der Name der Getter-Methode angegeben werden,
 * welcher den Primärschlüssel zurückliefert (ohne das vorangestellte "get"). Für den Merge-Test müssen die beiden Werte
 * 'mergePropertyName' - gibt den Namen der für den Merge-Test verwendeten Property an und 'mergeValue' - gibt den Wert
 * der Property für den Merge an, angegeben werden. Der angegebene Merge-Value, muss sich für einen erfolgreichen
 * Merge-Test vom Value der Testinstanz unterscheiden.
 * 
 * @param <T>
 * @param <PK>
 */
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractGenericDaoDbUnitTest<T, PK extends Serializable> extends AbstractDbUnitTest {

	/** Typ der persistenten Klasse */
	protected Class<T> type;

	/** Name des PK der persistenten Klassen mit führendem Großbuchstaben */
	protected String pkName;

	/** Name der Property, welche für den Merge-Test verwendet werden soll */
	protected String mergePropertyName;

	/** Der Wert welcher neu auf die Instanz gesetzt wird */
	protected Object mergeValue;

	/**
	 * Testet das Erstellen von Instanzen auf der Datenbank.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testCreate() throws InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

		// Instanz anlegen und prüfen dass der Primärschlüssel generiert wird
		T instanz = getGenericDao().create(getCreateTestInstanz());
		// Statement an die Datenbank absetzen
		getGenericDao().flush();

		assertNotNull(instanz);
		Method pkGetter = type.getMethod("get" + pkName, (Class<?>[])null);
		assertNotNull(pkGetter.invoke(instanz, (Object[])null));
	}

	/**
	 * Prüft das Auslesen der Instanz.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testRead() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		// Instanz anlegen und prüfen dass diese vorhanden ist
		T instanz = getGenericDao().create(getCreateTestInstanz());
		// Statement an die Datenbank absetzen und Objekte vom EM trennen
		getGenericDao().flush();
		getGenericDao().clear();

		assertNotNull(instanz);
		Method pkGetter = type.getMethod("get" + pkName, (Class<?>[])null);
		Serializable pk = (Serializable)pkGetter.invoke(instanz, (Object[])null);
		assertNotNull(pk);

		// Instanz löschen und prüfen dass diese nicht mehr vorhanden ist
		T instanzRead = getGenericDao().read(pk);
		assertNotNull(instanzRead);
	}

	/**
	 * Testet das Mergen von Instanzen. Hierfür müssen die Parameter 'mergePropertyName' und 'mergeValue' gesetzt sein.
	 * Werden diese Parameter nicht gesetzt, wird der Merge-Test nicht ausgeführt.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@Test
	public void testMerge() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		// Prüfung ob die Parameter die benötigt werden gesetzt sind.
		if (mergePropertyName != null && mergeValue != null) {
			// Instanz anlegen und prüfen dass diese vorhanden ist
			T instanz = getGenericDao().create(getCreateTestInstanz());
			// Statement an die Datenbank absetzen und Objekte von EM lösen
			getGenericDao().flush();
			getGenericDao().clear();

			assertNotNull(instanz);
			Method pkGetter = type.getMethod("get" + pkName, (Class<?>[])null);
			Serializable pk = (Serializable)pkGetter.invoke(instanz, (Object[])null);
			assertNotNull(pk);

			Method propertySetter = type.getMethod("set" + mergePropertyName, mergeValue.getClass());
			Method propertyGetter = type.getMethod("get" + mergePropertyName, (Class<?>[])null);

			// Prüfe das der alte und der neue Wert nicht gleich sind
			assertNotSame(propertyGetter.invoke(instanz, (Object[])null), mergeValue);

			// Setze den neuen Wert auf die Instanz und merge diese
			propertySetter.invoke(instanz, mergeValue);
			getGenericDao().update(instanz);
			// Statement an die Datenbank absetzen und Objekte von EM lösen
			getGenericDao().flush();
			getGenericDao().clear();

			// Lese die Instanz neu aus um sicherzustellen, dass die Änderung bis zur Datenbank
			// gekommen sind
			T instanzRead = getGenericDao().read(pk);
			assertEquals(propertyGetter.invoke(instanzRead, (Object[])null), mergeValue);
		}
		else {
			logger.info("Der Mergetest kann nicht ausgeführt werden");
			logger.info("mergePropertyName: '" + mergePropertyName + "'");
			logger.info("mergeValue: '" + mergeValue + "'");
		}
	}

	/**
	 * Testet das Löschen von Instanzen auf der Datenbank.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testRemove() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		// Instanz anlegen und prüfen dass diese vorhanden ist
		T instanz = getGenericDao().create(getCreateTestInstanz());

		// Statement an die Datenbank absetzen und Objekte von EM lösen
		getGenericDao().flush();
		getGenericDao().clear();

		assertNotNull(instanz);
		Method pkGetter = type.getMethod("get" + pkName, (Class<?>[])null);
		Serializable pk = (Serializable)pkGetter.invoke(instanz, (Object[])null);
		assertNotNull(pk);

		// Instanz löschen und prüfen dass diese nicht mehr vorhanden ist
		instanz = getGenericDao().read(pk);
		getGenericDao().delete(instanz);

		// Statement an die Datenbank absetzen und Objekte von EM lösen
		getGenericDao().flush();
		getGenericDao().clear();

		assertNull(getGenericDao().read(pk));
	}

	/**
	 * Erstellt eine Test-Instanz für das spezifische Dao und gibt dieses zurück.
	 * 
	 * @return die spezifische Instanz.
	 */
	public abstract T getCreateTestInstanz();

	// Getter / Setter

	public abstract GenericDao<T, Serializable> getGenericDao();

}
