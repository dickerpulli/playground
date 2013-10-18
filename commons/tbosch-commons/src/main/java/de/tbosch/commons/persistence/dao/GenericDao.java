package de.tbosch.commons.persistence.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import de.tbosch.commons.persistence.finder.FinderExecutor;

/**
 * Schnittstelle für ein generisches Dao mit den Standard CRUD-Methoden, sowie weiteren Funktionen für das Verwalten von
 * Entities. <br/>
 * <br/>
 * Der Schnittstelle wird der Typ der Klasse (T) übergeben, sowie der Typ des Primary-Key (PK). Der Typ des Primary-Key,
 * muss Serializable implementieren. <br/>
 * <br/>
 * Über die Repository-Annotation werden die Persistence-Exceptions in unchecked {@link DataAccessException} von Spring
 * gewrapped.
 * 
 * @param <T> Die Klasse für welche das Dao gilt.
 * @param <PK> der Typ des Primärschüssels.
 */
@Repository
public interface GenericDao<T, PK extends Serializable> extends FinderExecutor<T> {

	/**
	 * Speichert eine Instanz.
	 * 
	 * @param newInstance
	 * @return die gespeicherte Entity.
	 */
	public T create(T newInstance);

	/**
	 * Liest eine Entity anhand der Id aus.
	 * 
	 * @param id die eindeutige Id der Entity.
	 * @return die gefundene Entity oder null, wenn zur übergebenen Id keine Entity gefunden wurde.
	 */
	public T read(PK id);

	/**
	 * Aktualisert eine vorhandene Entity durch die übergebene.
	 * 
	 * @param transientObject
	 * 
	 * @return die zusammengeführte Entity (attached).
	 */
	public T update(T transientObject);

	/**
	 * Entfernt die übergebene Entity.
	 * 
	 * @param persistentObject
	 */
	public void delete(T persistentObject);

	/**
	 * Hole alle Entities.
	 * 
	 * @return alles
	 */
	public List<T> findAll();

	/**
	 * Flusht die Session und die Entities werden in die Datenbank geschrieben.
	 */
	public void flush();

	/**
	 * Clear bewirkt dass an die Session gebundene Entities "attached" von der Session gelöst werden und damit
	 * "detached" sind.
	 */
	public void clear();

	/**
	 * Räumt alle Elemente des Type's aus dem SecondLevel-Cache der SessionFactory auf.
	 */
	public void evict();

}
