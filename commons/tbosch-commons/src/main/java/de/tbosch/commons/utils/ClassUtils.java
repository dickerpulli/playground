package de.tbosch.commons.utils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Abstrakte Utils-Klasse mit Hilfsmethoden für die Verarbeitung von Klassen
 */
public abstract class ClassUtils {

	/**
	 * leerer privater Konstruktor, da Utility-Klasse
	 */
	private ClassUtils() {
		// empty
	}

	/**
	 * Prüft ob das Objekt von einem primitiven oder pseudo-primitiven Typ ist.
	 * 
	 * @param obj Das zu testende Objekt
	 * @return true, wenn das Objekt einen primitiven Typ repräsentiert oder ein eine Klasse, die als primitiv zu
	 *         behandeln ist, ansonsten false
	 */
	public static boolean isExtendedPrimitive(Object obj) {
		if (ClassUtils.isPrimitive(obj.getClass()) || (obj instanceof Date) || (obj instanceof BigDecimal)) {
			return true;
		}
		return false;
	}

	/**
	 * Nimmt eine Klasse entgegen und prüft ob es sich um einen primitven Typ wie int, boolean, ... oder um einen
	 * Wrapper-Typ wie {@link Integer}, {@link Boolean}, ... oder um eine Klasse vom {@link String} oder {@link Object}
	 * handelt
	 * 
	 * @return ob die übergebene Klasse ein primitiver Typ ist.
	 */
	public static boolean isPrimitive(Class<?> clazz) {

		// Prüfe auf Wrapper-Typen
		if (org.springframework.util.ClassUtils.isPrimitiveWrapper(clazz)) {
			return true;
		}

		// Prüfe auf primitive Typen
		if (clazz.isPrimitive()) {
			return true;
		}

		// Prüfe auf String
		if (clazz.equals(String.class)) {
			return true;
		}

		// Prüfe auf Class-Type
		if (clazz.equals(Class.class)) {
			return true;
		}

		return false;
	}

}
