package de.tbosch.commons.persistence.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

/**
 * Schnittstelle für ein generisches JPA Dao mit den Methoden zum setzen und holen eines <code>Entitymanager</code>.
 * 
 * @param <T> Die Klasse für welche das Dao gilt.
 * @param <PK> der Typ des Primärschüssels.
 */
@Repository
public interface GenericJpaDao<T, PK extends Serializable> extends GenericDao<T, PK> {

	/**
	 * Zugriff auf den Entity Manager.
	 * 
	 * @return Der Entity Manager der aktuellen Session.
	 */
	public EntityManager getEntityManager();

	/**
	 * Setzt den EntityManager
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager);

}
