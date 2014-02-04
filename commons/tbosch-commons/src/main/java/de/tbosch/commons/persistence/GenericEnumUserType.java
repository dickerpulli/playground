package de.tbosch.commons.persistence;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * Ein generischer Enum-Typ für die flexiblere Benamung von Enum-Konstanten. Eine Enumeration muss bei den Entity
 * Attributen mit diesem Typ mit _@Type anstatt _@Enumeration annotiert werden.<br>
 * Beispiel:<br>
 * _@Type(type = "de.tbosch.commons.persistence.GenericEnumUserType", parameters = {<br>
 * _@Parameter(name = "enumClass", value = "de.tbosch.commons.model.Entity"),<br>
 * _@Parameter(name = "identifierMethod", value = "getValue"),<br>
 * _@Parameter(name = "valueOfMethod", value = "fromValue") })<br>
 * <br>
 * Bei dieser Annotation wird eine Enumeration annotiert, deren Identifier über die <b>getValue</b>-Methode erreicht
 * wird und dessen default <b>valueOf</b> mit der <b>fromValue</b>-Methode den Enum Wert bekommt.
 * 
 * @author tbo
 */
public class GenericEnumUserType implements UserType, ParameterizedType {
	private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "name";

	private static final String DEFAULT_VALUE_OF_METHOD_NAME = "valueOf";

	@SuppressWarnings("rawtypes")
	private Class<? extends Enum> enumClass;

	private Class<?> identifierType;

	private Method identifierMethod;

	private Method valueOfMethod;

	private AbstractSingleColumnStandardBasicType type;

	private int[] sqlTypes;

	/**
	 * @see org.hibernate.usertype.ParameterizedType#setParameterValues(java.util.Properties)
	 */
	@Override
	public void setParameterValues(Properties parameters) {
		String enumClassName = parameters.getProperty("enumClass");
		try {
			enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
		} catch (ClassNotFoundException cfne) {
			throw new HibernateException("Enum class not found", cfne);
		}

		String identifierMethodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);

		try {
			identifierMethod = enumClass.getMethod(identifierMethodName, new Class[0]);
			identifierType = identifierMethod.getReturnType();
		} catch (Exception e) {
			throw new HibernateException("Failed to obtain identifier method", e);
		}

		type = (AbstractSingleColumnStandardBasicType) new TypeResolver().basic(identifierType.getName());

		if (type == null)
			throw new HibernateException("Unsupported identifier type " + identifierType.getName());

		sqlTypes = new int[] { type.sqlType() };

		String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);

		try {
			valueOfMethod = enumClass.getMethod(valueOfMethodName, new Class[] { identifierType });
		} catch (Exception e) {
			throw new HibernateException("Failed to obtain valueOf method", e);
		}
	}

	/**
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Class returnedClass() {
		return enumClass;
	}

	/**
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[],
	 *      org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object identifier = type.get(rs, names[0], session);
		if (identifier == null) {
			return null;
		}

		try {
			return valueOfMethod.invoke(enumClass, new Object[] { identifier });
		} catch (Exception e) {
			throw new HibernateException("Exception while invoking valueOf method '" + valueOfMethod.getName()
					+ "' of " + "enumeration class '" + enumClass + "'", e);
		}
	}

	/**
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int,
	 *      org.hibernate.engine.spi.SessionImplementor)
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		try {
			if (value == null) {
				st.setNull(index, type.sqlType());
			} else {
				Object identifier = identifierMethod.invoke(value, new Object[0]);
				type.set(st, identifier, index, session);
			}
		} catch (Exception e) {
			throw new HibernateException("Exception while invoking identifierMethod '" + identifierMethod.getName()
					+ "' of " + "enumeration class '" + enumClass + "'", e);
		}
	}

	/**
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	@Override
	public int[] sqlTypes() {
		return sqlTypes;
	}

	/**
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
	@Override
	public Object assemble(Serializable cached, Object owner) {
		return cached;
	}

	/**
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	@Override
	public Object deepCopy(Object value) {
		return value;
	}

	/**
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public Serializable disassemble(Object value) {
		return (Serializable) value;
	}

	/**
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(Object x, Object y) {
		return x == y;
	}

	/**
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode(Object x) {
		return x.hashCode();
	}

	/**
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	@Override
	public boolean isMutable() {
		return false;
	}

	/**
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) {
		return original;
	}

}