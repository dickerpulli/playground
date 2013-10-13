package de.tbosch.commons.persistence.validator;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

/**
 * Prüft einen Mindestwert für einen Wert
 * 
 * @author tbo
 */
@SuppressWarnings("serial")
public class MinDoubleValidator implements Validator<MinDouble>, PropertyConstraint, Serializable {

	private double min;

	/**
	 * @see org.hibernate.validator.Validator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(MinDouble parameters) {
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
			try {
				return new BigDecimal((String) value).compareTo(BigDecimal.valueOf(min)) >= 0;
			} catch (NumberFormatException nfe) {
				return false;
			}
		} else if ((value instanceof Double) || (value instanceof Float)) {
			double dv = ((Number) value).doubleValue();
			return dv >= min;
		} else if (value instanceof BigDecimal) {
			return ((BigDecimal) value).compareTo(BigDecimal.valueOf(min)) >= 0;
		} else if (value instanceof Number) {
			long lv = ((Number) value).longValue();
			return lv >= min;
		} else {
			return false;
		}
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
