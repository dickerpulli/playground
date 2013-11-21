package de.tbosch.commons.persistence.dao.standard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.tbosch.commons.persistence.dao.GenericHibernateDao;

/**
 * Hibernate-Implementierung der generischen Dao-Schnittstelle.
 * 
 * @param <T>
 *            Der Typ des generischen Daos
 * @param <PK>
 *            Der Typ des Primary Keys
 */
public class StandardGenericHibernateDao<T, PK extends Serializable> implements GenericHibernateDao<T, PK> {

	private final Log LOG = LogFactory.getLog(getClass());

	/** Der Typ der Klasse */
	private final Class<T> type;

	private final String prefixNamedQueries;

	private SessionFactory sessionFactory;

	/**
	 * Konstruktor der den Typ der Klasse übernimmt.
	 * 
	 * @param type
	 *            der Typ der Klasse
	 */
	public StandardGenericHibernateDao(Class<T> type) {
		this.type = type;
		this.prefixNamedQueries = type.getSimpleName() + ".";
	}

	/**
	 * Gibt die aktuelle Hibernate-Session zurück.
	 * 
	 * @return Session.
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#create(java.lang.Object)
	 */
	@Override
	public T create(T t) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO schreibt Objekt vom Typ " + type);

		getCurrentSession().persist(t);
		return t;
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#read(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T read(PK id) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO liest Objekt vom Typ " + type + " mit Schlüssel: " + id);

		return (T) getCurrentSession().get(type, id);
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#merge(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T update(T o) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO merged Objekt vom Typ " + type);

		return (T) getCurrentSession().merge(o);
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#remove(java.lang.Object)
	 */
	@Override
	public void delete(T o) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO löscht Objekt vom Typ " + type);

		getCurrentSession().delete(o);
	}

	/**
	 * @see de.tbosch.commons.persistence.finder.FinderExecutor#executeFinder(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodenName) {
		final String namedQueryName = getNamedQueryName(methodenName);
		return getCurrentSession().getNamedQuery(namedQueryName).list();
	}

	/**
	 * Ermittelt den Namen einer Named-Query aus dem Namen der aufgerufenen 'findXYZ'-Methode
	 * 
	 * @param methodenName
	 *            Name der aufgerufenen 'findXYZ'-Methode, z.B. 'findAlleUser'
	 * 
	 * @return Resultierende Name der Named-Query => Methodenname ergänzt um Model-Klasse, z.B. 'User.findAlleUser'
	 */
	private String getNamedQueryName(String methodenName) {
		final String namedQueryName = prefixNamedQueries + methodenName;
		if (LOG.isDebugEnabled()) {
			LOG.debug("Ermittelter Name der Named-Query:" + namedQueryName);
		}
		return namedQueryName;
	}

	/**
	 * @see de.tbosch.commons.persistence.finder.FinderExecutor#executeFinder(java.lang.String, java.lang.Object[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodenName, final Object[] queryArgs) {
		final String namedQueryName = getNamedQueryName(methodenName);
		Query namedQuery = getCurrentSession().getNamedQuery(namedQueryName);
		for (int i = 0; i < queryArgs.length; i++) {
			namedQuery.setParameter(i, queryArgs[i]);
		}
		return namedQuery.list();
	}

	/**
	 * @see de.tbosch.commons.persistence.finder.FinderExecutor#executeFinder(java.lang.String, java.util.Map)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodenName, final Map<String, Object> queryArgs) {
		final String namedQueryName = getNamedQueryName(methodenName);
		Query namedQuery = getCurrentSession().getNamedQuery(namedQueryName);
		for (Entry<String, Object> queryArg : queryArgs.entrySet()) {
			namedQuery.setParameter(queryArg.getKey(), queryArg.getValue());
		}
		return namedQuery.list();
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#clear()
	 */
	@Override
	public void clear() {
		getCurrentSession().clear();
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#flush()
	 */
	@Override
	public void flush() {
		getCurrentSession().flush();
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return getCurrentSession().createCriteria(type).list();
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#evict()
	 */
	@Override
	public void evict() {
		throw new UnsupportedOperationException("Wird noch nicht unterstützt");
	}

	// Getter / Setter

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericHibernateDao#getSessionFactory()
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericHibernateDao#setSessionFactory(org.hibernate.SessionFactory)
	 */
	@Override
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
