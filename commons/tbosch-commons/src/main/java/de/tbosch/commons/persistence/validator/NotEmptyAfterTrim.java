package de.tbosch.commons.persistence.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

/**
 * Testet das Leersein eines Strings (nicht NULL und Länge > 0). Alle Leerzeichen und sonstigen Steuerzeichen werden
 * ignoriert.
 * 
 * @author tbo
 */
@Documented
@ValidatorClass(NotEmptyAfterTrimValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyAfterTrim {
	/**
	 * Gibt die Fehlermeldung zurück.
	 * 
	 * @return
	 */
	String message() default "{validator.notEmptyAfterTrim}";
}
