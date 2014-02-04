package de.tbosch.commons.persistence.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

/**
 * Testet das Leersein eines Strings (nicht NULL und Länge > 0). Alle Leerzeichen und sonstigen Steuerzeichen werden
 * ignoriert.
 * 
 * @author tbo
 */
@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyAfterTrim {
	/**
	 * Gibt die Fehlermeldung zurück.
	 * 
	 * @return
	 */
	String message() default "{validator.notEmptyAfterTrim}";
}
