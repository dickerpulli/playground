package de.tbosch.commons.test;

import javax.annotation.PostConstruct;

import org.easymock.EasyMock;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;

/**
 * FactoryBean für das Erzeugen von EasyMock-Proxies über den Spring ApplicationContext.
 * 
 * @see org.easymock.EasyMock#createMock(Class)
 * @see org.easymock.EasyMock#createNiceMock(Class)
 * @see org.easymock.EasyMock#createStrictMock(Class)
 * @author Thomas Bosch
 */
public class EasyMockFactoryBean<T> implements FactoryBean<T> {

	private Class<T> type;

	private boolean niceMock;

	private boolean strictMock;

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public T getObject() throws Exception {
		if (niceMock) {
			return EasyMock.createNiceMock(type);
		}
		if (strictMock) {
			return EasyMock.createStrictMock(type);
		}
		return EasyMock.createMock(type);
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return type;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Nach dem Erzeugen im der Bean aufgerufen. Prüft die Attribute der Klasse.
	 */
	@PostConstruct
	public void postConstruct() {
		if (type == null) {
			throw new BeanCreationException("property 'type' must be set");
		}
		if (niceMock && strictMock) {
			throw new BeanCreationException("either 'niceMock' or 'strictMock' can be true");
		}
	}

	/**
	 * Der Typ, den das EasyMock Proxy implementiert.
	 * 
	 * @param type Das Interface
	 */
	public void setType(Class<T> type) {
		this.type = type;
	}

	/**
	 * Sagt, ob das EasyMock Proxy ein NiceMock ist.
	 * 
	 * @see org.easymock.NiceMock
	 * @param niceMock boolean
	 */
	public void setNiceMock(boolean niceMock) {
		this.niceMock = niceMock;
	}

	/**
	 * Sagt, ob das EasyMock Proxy ein StrickMock ist.
	 * 
	 * @see org.easymock.StrictMock
	 * @param strictMock boolean
	 */
	public void setStrictMock(boolean strictMock) {
		this.strictMock = strictMock;
	}

}
