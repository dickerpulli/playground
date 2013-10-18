package de.tbosch.commons.persistence.dao.standard;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.tbosch.commons.persistence.dao.GenericDao;
import de.tbosch.commons.persistence.dao.GenericJpaDao;

/**
 * Dieser FinderDispatcher findet alle Methoden der GenericDao<T, PK> Instanzen, die mit `find' beginnen und mappt diese
 * auf die entprechenden Methoden in der orm.xml
 * 
 * @param <T>
 * @param <PK>
 */
public class FinderDispatcherGenericJpaDao<T, PK extends Serializable> extends
		AbstractFinderDispatcherGenericDao<T, PK> {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public GenericDao<T, PK> getObject() throws Exception {
		if (genericDao == null) {
			genericDao = new StandardGenericJpaDao<T, PK>(entityClass);
		}
		((GenericJpaDao<T, PK>)genericDao).setEntityManager(entityManager);
		return super.getObject();
	}

}
