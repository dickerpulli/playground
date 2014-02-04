//---------------------------------------------------------------------------
// (C) 2009 ÖRAG Rechtsschutz AG, ÖRAG Service GmbH //
//---------------------------------------------------------------------------
package de.tbosch.commons.persistence.dao.standard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

import de.tbosch.commons.persistence.dao.GenericDao;
import de.tbosch.commons.persistence.finder.FinderExecutor;

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
public abstract class AbstractFinderDispatcherGenericDao<T, PK extends Serializable> implements
		FactoryBean<GenericDao<T, PK>> {

	private Class<GenericDao<T, PK>> daoInterface;

	protected Class<T> entityClass;

	protected GenericDao<T, PK> genericDao;

	public void setDaoInterface(Class<GenericDao<T, PK>> daoInterface) {
		this.daoInterface = daoInterface;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public void setGenericDao(GenericDao<T, PK> genericDao) {
		this.genericDao = genericDao;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GenericDao<T, PK> getObject() throws Exception {
		ProxyFactory result = new ProxyFactory();
		result.setTarget(genericDao);
		result.setInterfaces(new Class[] { daoInterface });
		result.addAdvice(new MethodInterceptor() {
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				String methodName = invocation.getMethod().getName();
				if (methodName.startsWith("find")) {
					FinderExecutor<T> target = (FinderExecutor<T>)invocation.getThis();
					// Hier wird die execute-Methode auf dem Finder dynamisch
					// aufgerufen, um anhand der Signatur unterschiedliche
					// Methoden anzusprechen (z.B. mit Object[] oder mit Map)
					// Damit ist es möglich, DAO-Interfaces für NamedQueries
					// auch mit einer Map von von benannten Parametern generisch
					// einzusetzen.
					Object result;
					if (invocation.getArguments() == null
							|| invocation.getArguments().length == 0
							|| (invocation.getArguments().length == 1 && (invocation.getMethod().getParameterTypes()[0]
									.equals(Map.class) || invocation.getMethod().getParameterTypes()[0]
									.equals(Object[].class)))) {
						// Haben wir keine Argumente, oder ein Object[] oder
						// eine Map rufen wir die dedizierte executeFinder
						// Methode auf
						result = MethodUtils.invokeMethod(target, "executeFinder", ArrayUtils.addAll(
								new Object[] { methodName }, invocation.getArguments()), ArrayUtils.addAll(
								new Class[] { String.class }, invocation.getMethod().getParameterTypes()));
					}
					else {
						// Haben wir eine andere Methodensignatur rufen wir
						// immer executeFinder(String, Object...) auf
						result = MethodUtils.invokeMethod(target, "executeFinder", new Object[] { methodName,
								invocation.getArguments() },
								ArrayUtils.addAll(new Class[] { String.class }, new Class[] { Object[].class }));
					}

					// Liefern der Liste oder des ersten Elements, abhängig vom
					// Return-Type
					if (invocation.getMethod().getReturnType().isAssignableFrom(List.class)) {
						return result;
					}
					else {
						List<GenericDao<T, PK>> list = (List<GenericDao<T, PK>>)result;
						return list.size() == 0 ? null : list.get(0);
					}

				}
				else {
					return invocation.proceed();
				}
			}
		});
		return (GenericDao<T, PK>)result.getProxy();
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<GenericDao<T, PK>> getObjectType() {
		return daoInterface;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
