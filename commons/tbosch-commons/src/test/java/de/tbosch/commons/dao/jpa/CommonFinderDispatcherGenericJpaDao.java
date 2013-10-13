package de.tbosch.commons.dao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.tbosch.commons.persistence.dao.standard.FinderDispatcherGenericJpaDao;

public class CommonFinderDispatcherGenericJpaDao<T, PK extends Serializable> extends FinderDispatcherGenericJpaDao<T, PK>  {

	@PersistenceContext
	@Override
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}
	
}
