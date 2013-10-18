package de.tbosch.commons.persistence.dao.standard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import de.tbosch.commons.persistence.dao.GenericHibernateDao;

/**
 * Hibernate-Implementierung der generischen Dao-Schnittstelle.
 * 
 * @param <T> Der Typ des generischen Daos
 * @param <PK> Der Typ des Primary Keys
 */
public class StandardGenericHibernateDao<T, PK extends Serializable> extends HibernateDaoSupport implements
		GenericHibernateDao<T, PK> {

	private final Log LOG = LogFactory.getLog(getClass());

	/** Der Typ der Klasse */
	private final Class<T> type;

	private final String prefixNamedQueries;

	/**
	 * Konstruktor der den Typ der Klasse übernimmt.
	 * 
	 * @param type der Typ der Klasse
	 */
	public StandardGenericHibernateDao(Class<T> type) {
		this.type = type;
		this.prefixNamedQueries = type.getSimpleName() + ".";
	}

	/**
	 * SessionFactory an das DaoSupport weitergeben.
	 */
	@Autowired
	public void setSessionFabrik(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#create(java.lang.Object)
	 */
	@Override
	public T create(T t) {
		if (LOG.isDebugEnabled()) LOG.debug("GenericDAO schreibt Objekt vom Typ " + type);

		getHibernateTemplate().persist(t);
		return t;
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#read(java.io.Serializable)
	 */
	@Override
	public T read(PK id) {
		if (LOG.isDebugEnabled()) LOG.debug("GenericDAO liest Objekt vom Typ " + type + " mit Schlüssel: " + id);

		return getHibernateTemplate().get(type, id);
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#merge(java.lang.Object)
	 */
	@Override
	public T update(T o) {
		if (LOG.isDebugEnabled()) LOG.debug("GenericDAO merged Objekt vom Typ " + type);

		return getHibernateTemplate().merge(o);
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#remove(java.lang.Object)
	 */
	@Override
	public void delete(T o) {
		if (LOG.isDebugEnabled()) LOG.debug("GenericDAO löscht Objekt vom Typ " + type);

		getHibernateTemplate().delete(o);
	}

	/**
	 * @see de.tbosch.commons.persistence.finder.FinderExecutor#executeFinder(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodenName) {
		final String namedQueryName = getNamedQueryName(methodenName);
		return getHibernateTemplate().findByNamedQuery(namedQueryName);
	}

	/**
	 * Ermittelt den Namen einer Named-Query aus dem Namen der aufgerufenen 'findXYZ'-Methode
	 * 
	 * @param methodenName Name der aufgerufenen 'findXYZ'-Methode, z.B. 'findAlleUser'
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
		return getHibernateTemplate().findByNamedQuery(namedQueryName, queryArgs);
	}

	/**
	 * @see de.tbosch.commons.persistence.finder.FinderExecutor#executeFinder(java.lang.String, java.util.Map)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodenName, final Map<String, Object> queryArgs) {
		final String namedQueryName = getNamedQueryName(methodenName);
		String[] keys = new String[queryArgs.size()];
		Object[] values = new Object[queryArgs.size()];
		int pos = 0;
		for (Entry<String, Object> entry : queryArgs.entrySet()) {
			keys[pos] = entry.getKey();
			values[pos] = entry.getValue();
			pos++;
		}

		return getHibernateTemplate().findByNamedQueryAndNamedParam(namedQueryName, keys, values);
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#clear()
	 */
	@Override
	public void clear() {
		getHibernateTemplate().clear();
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#flush()
	 */
	@Override
	public void flush() {
		getHibernateTemplate().flush();
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return getHibernateTemplate().find("from " + type.getName());
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#evict()
	 */
	@Override
	public void evict() {
		throw new UnsupportedOperationException("Wird noch nicht unterstützt");
	}
}
