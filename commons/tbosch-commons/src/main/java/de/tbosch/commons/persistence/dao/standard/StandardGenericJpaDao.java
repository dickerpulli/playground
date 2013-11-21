package de.tbosch.commons.persistence.dao.standard;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.internal.SessionFactoryImpl;

import de.tbosch.commons.persistence.dao.GenericJpaDao;

/**
 * Jpa-Implementierung der generischen Dao-Schnittstelle.
 * 
 * @param <T>
 * @param <PK>
 */
public class StandardGenericJpaDao<T, PK extends Serializable> implements GenericJpaDao<T, PK> {

	/** Logger-Objekt */
	private static final Log LOG = LogFactory.getLog(StandardGenericJpaDao.class);

	/** Der Typ der Klasse */
	private final Class<T> type;

	/** EntityManager für die Verwaltung der Entities */
	private EntityManager entityManager;

	/**
	 * Konstruktor der den Typ der Klasse �bernimmt.
	 * 
	 * @param type
	 *            der Typ der Klasse
	 */
	public StandardGenericJpaDao(Class<T> type) {
		this.type = type;
	}

	/**
	 * @see de.tbosch.common.persistence.dao.GenericDao#create(java.lang.Object)
	 */
	@Override
	public T create(T newInstance) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO schreibt Objekt vom Typ " + type);

		entityManager.persist(newInstance);
		return newInstance;
	}

	/**
	 * @see de.tbosch.common.persistence.dao.GenericDao#remove(java.lang.Object)
	 */
	@Override
	public void delete(T transientObject) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO l�scht Objekt vom Typ " + type);

		entityManager.remove(transientObject);
	}

	/**
	 * @see de.tbosch.common.persistence.dao.GenericDao#merge(java.lang.Object)
	 */
	@Override
	public T update(T transientObject) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO merged Objekt vom Typ " + type);

		return entityManager.merge(transientObject);
	}

	/**
	 * @see de.tbosch.common.persistence.dao.GenericDao#read(java.io.Serializable)
	 */
	@Override
	public T read(PK id) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO liest Objekt vom Typ " + type + " mit Schl�ssel: " + id);

		return entityManager.find(type, id);
	}

	/**
	 * @see de.tbosch.common.persistence.dao.GenericDao#clear()
	 */
	@Override
	public void clear() {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO clear");

		entityManager.clear();
	}

	/**
	 * @see de.tbosch.common.persistence.dao.GenericDao#evict()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void evict() {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO evict");

		// Bereinige den Cache
		HibernateEntityManager hibernateEntityManager = (HibernateEntityManager) entityManager;
		Session session = hibernateEntityManager.getSession();
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) session.getSessionFactory();
		Map cacheRegionen = sessionFactoryImpl.getAllSecondLevelCacheRegions();
		Collection<Cache> cacheRegion = cacheRegionen.values();
		for (Cache cache : cacheRegion) {
			cache.evictCollectionRegions();
			cache.evictDefaultQueryRegion();
			cache.evictEntityRegions();
			cache.evictNaturalIdRegions();
			cache.evictQueryRegions();
		}
	}

	/**
	 * @see de.tbosch.common.persistence.dao.GenericDao#flush()
	 */
	@Override
	public void flush() {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO flush");

		entityManager.flush();
	}

	/**
	 * @see de.tbosch.common.persistence.finder.FinderExecutor#executeFinder(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodenName) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO führt Named-Query aus:" + methodenName);

		String queryName = type.getSimpleName() + "." + methodenName;
		Query namedQuery = entityManager.createNamedQuery(queryName);
		return namedQuery.getResultList();
	}

	/**
	 * @see de.tbosch.common.persistence.finder.FinderExecutor#executeFinder(java.lang.String, java.util.Map)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodenName, Map<String, Object> queryArgs) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO führt Named-Query aus:" + methodenName);

		if (queryArgs == null || queryArgs.size() == 0) {
			throw new IllegalArgumentException();
		}

		String queryName = type.getSimpleName() + "." + methodenName;
		Query namedQuery = entityManager.createNamedQuery(queryName);

		for (String key : queryArgs.keySet()) {
			namedQuery.setParameter(key, queryArgs.get(key));
		}

		return namedQuery.getResultList();
	}

	/**
	 * @see de.tbosch.common.persistence.finder.FinderExecutor#executeFinder(java.lang.String, java.lang.Object[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String methodeName, final Object[] queryArgs) {
		final Query namedQuery = prepareQuery(methodeName, queryArgs);
		return namedQuery.getResultList();
	}

	/**
	 * Bereitet das Query für die Ausführung vor.
	 * 
	 * @param methodName
	 *            der Name der Methode
	 * @param queryArgs
	 *            die Suchparameter
	 * @return das vorbereitete Query.
	 */
	private Query prepareQuery(String methodName, Object[] queryArgs) {
		if (LOG.isDebugEnabled())
			LOG.debug("GenericDAO führt Named-Query aus:" + methodName);

		String queryName = type.getSimpleName() + "." + methodName;
		Query namedQuery = entityManager.createNamedQuery(queryName);
		if (queryArgs != null) {
			for (int i = 0; i < queryArgs.length; i++) {
				if (LOG.isDebugEnabled())
					LOG.debug("Named-Query " + methodName + " Parameter " + (i + 1) + ": " + queryArgs[i]);

				namedQuery.setParameter(i + 1, queryArgs[i]);
			}
		}
		return namedQuery;
	}

	/**
	 * @see de.tbosch.commons.persistence.dao.GenericDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return entityManager.createQuery("select t from " + type.getName() + " t").getResultList();
	}

	// Getter / Setter

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
