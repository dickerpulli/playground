package de.tbosch.commons.persistence.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

/**
 * Testet die minimale Größe eines Elements. Auch double-Werte sind vergleichbar.
 * 
 * @author tbo
 */
@Documented
@ValidatorClass(MinDoubleValidator.class)
@Target( { METHOD, FIELD })
@Retention(RUNTIME)
public @interface MinDouble {
	/**
	 * Der Wert
	 * 
	 * @return
	 */
	double value();

	/**
	 * Fehlermeldung
	 * 
	 * @return
	 */
	String message() default "{validator.min}";
}
