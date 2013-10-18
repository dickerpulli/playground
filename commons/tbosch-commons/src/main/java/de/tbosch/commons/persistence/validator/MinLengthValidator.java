package de.tbosch.commons.persistence.validator;

import java.io.Serializable;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

/**
 * Prüft die Mindestlänge für einen String.
 * 
 * @author tbo
 */
@SuppressWarnings("serial")
public class MinLengthValidator implements Validator<MinLength>, PropertyConstraint, Serializable {

	private int min;

	/**
	 * @see org.hibernate.validator.Validator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(MinLength parameters) {
		min = parameters.value();
	}

	/**
	 * @see org.hibernate.validator.Validator#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return true;
		if (value instanceof String) {
			if (((String) value).length() >= min)
				return true;
		}
		return false;
	}

	/**
	 * @see org.hibernate.validator.PropertyConstraint#apply(org.hibernate.mapping.Property)
	 */
	@Override
	public void apply(Property property) {
		Column col = (Column) property.getColumnIterator().next();
		col.setCheckConstraint(col.getName() + ">=" + min);
	}
}
