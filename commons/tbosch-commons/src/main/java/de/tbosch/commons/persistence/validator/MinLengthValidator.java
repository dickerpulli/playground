package de.tbosch.commons.persistence.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Prüft die Mindestlänge für einen String.
 * 
 * @author tbo
 */
public class MinLengthValidator implements ConstraintValidator<MinLength, String> {

	private int min;

	/**
	 * @see org.hibernate.validator.Validator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(MinLength parameters) {
		min = parameters.value();
	}

	/**
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;
		if (value.length() >= min)
			return true;
		return false;
	}

}
