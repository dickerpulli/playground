package de.tbosch.commons.persistence.finder;

import java.util.List;
import java.util.Map;

/**
 * Schnittstelle für die Suche über den Methodennamen.
 * 
 * @param <T>
 */
public interface FinderExecutor<T> {

	/**
	 * Sucht über den übergebenen Methodennamen und die Parameter nach den Entities.
	 * 
	 * @param methodenName
	 *            den Namen der Methode
	 * @return die Liste der gefundenen Entities oder eine leere Liste, wenn keine Entities gefunden wurden.
	 */
	public List<T> executeFinder(String methodenName);

	/**
	 * Sucht �ber den übergebenen Methodennamen und die Parameter nach den Entities.
	 * 
	 * @param methodenName
	 *            den Namen der Methode
	 * @param queryArgs
	 *            die Suchparameter
	 * @return die Liste der gefundenen Entities oder eine leere Liste, wenn keine Entities gefunden wurden.
	 */
	public List<T> executeFinder(String methodenName, Object[] queryArgs);

	/**
	 * Sucht über den übergebenen Methodennamen und die Parameter nach den Entities.
	 * 
	 * @param methodenName
	 *            den Namen der Methode
	 * @param queryArgs
	 *            die Suchparameter als key/value Paare, die Map darf nicht <code>null</code> sein oder leer
	 * @return die Liste der gefundenen Entities oder eine leere Liste, wenn keine Entities gefunden wurden.
	 */
	public List<T> executeFinder(String methodenName, Map<String, Object> queryArgs);

}