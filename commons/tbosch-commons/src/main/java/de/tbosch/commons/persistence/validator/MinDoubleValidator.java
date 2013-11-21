package de.tbosch.commons.persistence.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Prüft einen Mindestwert für einen Wert
 * 
 * @author tbo
 */
public class MinDoubleValidator implements ConstraintValidator<MinDouble, Object> {

	private double min;

	/**
	 * @see org.hibernate.validator.Validator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(MinDouble parameters) {
		min = parameters.value();
	}

	/**
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
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

}
