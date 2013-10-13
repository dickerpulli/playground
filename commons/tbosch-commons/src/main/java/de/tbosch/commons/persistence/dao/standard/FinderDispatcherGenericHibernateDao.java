//---------------------------------------------------------------------------
// (C) 2009 ÖRAG Rechtsschutz AG, ÖRAG Service GmbH //
//---------------------------------------------------------------------------
package de.tbosch.commons.persistence.dao.standard;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.tbosch.commons.persistence.dao.GenericDao;
import de.tbosch.commons.persistence.dao.GenericHibernateDao;

/**
 * Der FinderDispatcher ist eine Factory für das generische Dao. Die Methode <b>getObject()</b> liefert ein Objekt vom
 * Typ GenericHibernateDao. Jede Methode in diesem Objekt, welche mit <b>find</b> beginnt wird an die Methode
 * <b>executeFinder</b> desselben Objekts weitergeleitet.
 * 
 * @author tbo
 * 
 * @param <T> Der Entity-Typ
 * @param <PK> Der PrimaryKey-Typ
 */
public class FinderDispatcherGenericHibernateDao<T, PK extends Serializable> extends
		AbstractFinderDispatcherGenericDao<T, PK> {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public GenericDao<T, PK> getObject() throws Exception {
		if (genericDao == null) {
			genericDao = new StandardGenericHibernateDao<T, PK>(entityClass);
		}
		((GenericHibernateDao<T, PK>)genericDao).setSessionFactory(sessionFactory);
		return super.getObject();
	}

}
