package de.tbosch.commons.persistence.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Schnittstelle für ein generisches Hibernate Dao mit den Methoden zum setzen und holen eines
 * <code>SessionFactory</code>.
 * 
 * @param <T> Die Klasse für welche das Dao gilt.
 * @param <PK> der Typ des Primärschüssels.
 */
@Repository
public interface GenericHibernateDao<T, PK extends Serializable> extends GenericDao<T, PK> {

	/**
	 * Holt die SessionFactory
	 * 
	 * @return Die SessionFactory
	 */
	public SessionFactory getSessionFactory();

	/**
	 * Setzt die SessionFactory
	 * 
	 * @param sessionFactory Die SessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory);

}
