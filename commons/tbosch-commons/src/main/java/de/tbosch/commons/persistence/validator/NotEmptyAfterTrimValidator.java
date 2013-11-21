package de.tbosch.commons.persistence.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Testet das Leersein eines Strings bei Ignorierung von Steuer- und Leerzeichen.
 * 
 * @author tbo
 */
public class NotEmptyAfterTrimValidator implements ConstraintValidator<NotEmptyAfterTrim, String> {

	/**
	 * @see org.hibernate.validator.Validator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(NotEmptyAfterTrim parameters) {
		// gewollt leer
	}

	/**
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return false;
		else
			return value.trim().length() > 0;
	}

}
